package com.automifychat.app.tag;

import com.automifychat.app.community.Community;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class TagService {

    private static final Pattern VALID = Pattern.compile("^[a-z0-9][a-z0-9-]{0,38}[a-z0-9]?$");

    private final TagRepository tags;

    public TagService(TagRepository tags) {
        this.tags = tags;
    }

    /**
     * Parse a free-form tag string (comma or whitespace separated) into a set of
     * normalized Tag entities under the given community, creating new ones as needed.
     */
    @Transactional
    public Set<Tag> resolveOrCreate(Community community, String rawTags) {
        if (rawTags == null || rawTags.isBlank()) return Set.of();
        Set<Tag> result = new LinkedHashSet<>();
        for (String raw : rawTags.toLowerCase().split("[,\\s]+")) {
            String name = raw.trim();
            if (name.isEmpty()) continue;
            if (!VALID.matcher(name).matches()) continue;
            Tag tag = tags.findByCommunityIdAndName(community.getId(), name)
                    .orElseGet(() -> tags.save(new Tag(community, name)));
            result.add(tag);
            if (result.size() >= 5) break;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Tag> tagsForCommunity(Long communityId) {
        return tags.findByCommunityIdOrderByNameAsc(communityId);
    }
}
