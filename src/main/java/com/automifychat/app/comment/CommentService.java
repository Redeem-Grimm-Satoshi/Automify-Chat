package com.automifychat.app.comment;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.notification.NotificationService;
import com.automifychat.app.post.Post;
import com.automifychat.app.post.PostRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentService {

    private final CommentRepository comments;
    private final PostRepository posts;
    private final UserAccountRepository users;
    private final NotificationService notificationService;

    public CommentService(CommentRepository comments,
                          PostRepository posts,
                          UserAccountRepository users,
                          NotificationService notificationService) {
        this.comments = comments;
        this.posts = posts;
        this.users = users;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Comment> commentsForPost(Long postId) {
        return comments.findForPost(postId);
    }

    @Transactional(readOnly = true)
    public List<Comment> commentsByAuthor(String username) {
        return comments.findByAuthor(username);
    }

    @Transactional
    public Comment createComment(String authorUsername, Long postId, Long parentCommentId, String bodyMd) {
        UserAccount author = users.findByUsername(authorUsername)
                .orElseThrow(() -> new IllegalStateException("No user: " + authorUsername));
        Post post = posts.findById(postId)
                .orElseThrow(() -> new IllegalStateException("No post: " + postId));
        UserAccount parentAuthor = null;
        if (parentCommentId != null) {
            Comment parent = comments.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalStateException("No parent comment: " + parentCommentId));
            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalArgumentException("Parent comment belongs to a different post");
            }
            parentAuthor = parent.getAuthor();
        }
        Comment saved = comments.save(new Comment(post, author, parentCommentId, bodyMd));
        notificationService.onCommentCreated(saved, parentCommentId, parentAuthor);
        return saved;
    }

    @Transactional
    public void softDelete(Long commentId, String requesterUsername) {
        Comment c = comments.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment not found: " + commentId));
        if (!c.getAuthor().getUsername().equals(requesterUsername)) {
            throw new AccessDeniedException("Only the author can delete this comment");
        }
        c.markDeleted();
    }

    /**
     * Tree node with depth for nested rendering. Deleted comments are kept in the tree
     * (as placeholders) to preserve thread structure for descendants.
     */
    public record Node(Comment comment, int depth, boolean accepted) {}

    public List<Node> toTree(List<Comment> flat, Long acceptedAnswerCommentId) {
        Map<Long, List<Comment>> byParent = new HashMap<>();
        for (Comment c : flat) {
            byParent.computeIfAbsent(c.getParentCommentId(), k -> new ArrayList<>()).add(c);
        }
        List<Node> out = new ArrayList<>();
        if (acceptedAnswerCommentId != null) {
            flat.stream()
                .filter(c -> acceptedAnswerCommentId.equals(c.getId()))
                .findFirst()
                .ifPresent(accepted -> walk(out, accepted, 0, byParent, acceptedAnswerCommentId, true));
        }
        for (Comment c : byParent.getOrDefault(null, List.of())) {
            if (acceptedAnswerCommentId == null || !acceptedAnswerCommentId.equals(c.getId())) {
                walk(out, c, 0, byParent, acceptedAnswerCommentId, false);
            }
        }
        return out;
    }

    private void walk(List<Node> out, Comment c, int depth,
                      Map<Long, List<Comment>> byParent, Long acceptedId, boolean isAccepted) {
        out.add(new Node(c, depth, isAccepted));
        for (Comment child : byParent.getOrDefault(c.getId(), List.of())) {
            walk(out, child, depth + 1, byParent, acceptedId,
                 acceptedId != null && acceptedId.equals(child.getId()));
        }
    }

    public record NewCommentForm(
            @NotBlank @Size(min = 1, max = 10000) String body,
            Long parentCommentId
    ) {}
}
