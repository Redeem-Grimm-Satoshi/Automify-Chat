package com.automifychat.app.post;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.community.Community;
import com.automifychat.app.community.CommunityRepository;
import com.automifychat.app.notification.NotificationService;
import com.automifychat.app.tag.Tag;
import com.automifychat.app.tag.TagService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    private static final int FEED_SIZE = 50;

    private final PostRepository posts;
    private final UserAccountRepository users;
    private final CommunityRepository communities;
    private final TagService tagService;
    private final NotificationService notificationService;

    public PostService(PostRepository posts,
                       UserAccountRepository users,
                       CommunityRepository communities,
                       TagService tagService,
                       NotificationService notificationService) {
        this.posts = posts;
        this.users = users;
        this.communities = communities;
        this.tagService = tagService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Post> globalFeed(String sort) {
        var page = PageRequest.of(0, FEED_SIZE);
        return "top".equalsIgnoreCase(sort) ? posts.findTopFeed(page) : posts.findRecentFeed(page);
    }

    @Transactional(readOnly = true)
    public List<Post> feedForCommunity(Long communityId, String sort) {
        var page = PageRequest.of(0, FEED_SIZE);
        return "top".equalsIgnoreCase(sort)
                ? posts.findTopForCommunity(communityId, page)
                : posts.findRecentForCommunity(communityId, page);
    }

    @Transactional(readOnly = true)
    public List<Post> feedForCommunityAndTag(Long communityId, String tag) {
        return posts.findByCommunityAndTag(communityId, tag.toLowerCase(), PageRequest.of(0, FEED_SIZE));
    }

    @Transactional(readOnly = true)
    public List<Post> postsByAuthor(String username) {
        return posts.findByAuthor(username, PageRequest.of(0, 10));
    }

    @Transactional(readOnly = true)
    public List<Post> search(String query) {
        if (query == null || query.isBlank()) return List.of();
        return posts.searchByText(query.trim(), PageRequest.of(0, 30));
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return posts.findByIdWithRelations(id);
    }

    @Transactional
    public Post createPost(String authorUsername, String communitySlug,
                           String title, String bodyMd, PostType type, String rawTags) {
        UserAccount author = users.findByUsername(authorUsername)
                .orElseThrow(() -> new IllegalStateException("Author not found: " + authorUsername));
        Community community = communities.findBySlug(communitySlug)
                .orElseThrow(() -> new IllegalStateException("Community not found: " + communitySlug));
        Post post = new Post(community, author, title, bodyMd, type);
        Set<Tag> tags = tagService.resolveOrCreate(community, rawTags);
        post.getTags().addAll(tags);
        Post saved = posts.save(post);
        notificationService.onPostCreated(saved);
        return saved;
    }

    @Transactional
    public void softDelete(Long postId, String requesterUsername) {
        Post post = posts.findById(postId)
                .orElseThrow(() -> new IllegalStateException("Post not found: " + postId));
        if (!post.getAuthor().getUsername().equals(requesterUsername)) {
            throw new org.springframework.security.access.AccessDeniedException("Only the author can delete this post");
        }
        post.markDeleted();
    }
}
