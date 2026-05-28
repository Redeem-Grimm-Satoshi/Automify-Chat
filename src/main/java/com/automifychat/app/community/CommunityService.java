package com.automifychat.app.community;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CommunityService {

    private static final Pattern SLUG = Pattern.compile("^[a-z0-9][a-z0-9-]{1,38}[a-z0-9]$");

    private final CommunityRepository communities;
    private final MembershipRepository memberships;
    private final UserAccountRepository users;

    public CommunityService(CommunityRepository communities,
                            MembershipRepository memberships,
                            UserAccountRepository users) {
        this.communities = communities;
        this.memberships = memberships;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<Community> all() {
        return communities.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Community> findBySlug(String slug) {
        return communities.findBySlug(slug);
    }

    @Transactional
    public Community createCommunity(String ownerUsername, String slug, String name, String description) {
        if (!SLUG.matcher(slug).matches()) {
            throw new CommunityException("Slug must be 3–40 chars, lowercase letters, digits, hyphens");
        }
        if (communities.existsBySlug(slug)) {
            throw new CommunityException("Community '" + slug + "' already exists");
        }
        UserAccount owner = users.findByUsername(ownerUsername)
                .orElseThrow(() -> new IllegalStateException("No user: " + ownerUsername));
        Community c = communities.save(new Community(slug, name, description, owner));
        memberships.save(new Membership(owner.getId(), c.getId(), Membership.Role.OWNER));
        return c;
    }

    @Transactional
    public void join(String username, String slug) {
        UserAccount user = users.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("No user: " + username));
        Community c = communities.findBySlug(slug)
                .orElseThrow(() -> new CommunityException("Community not found: " + slug));
        if (!memberships.existsByUserIdAndCommunityId(user.getId(), c.getId())) {
            memberships.save(new Membership(user.getId(), c.getId(), Membership.Role.MEMBER));
        }
    }

    public static class CommunityException extends RuntimeException {
        public CommunityException(String message) { super(message); }
    }
}
