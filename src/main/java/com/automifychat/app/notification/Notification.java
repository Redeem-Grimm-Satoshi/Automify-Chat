package com.automifychat.app.notification;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.comment.Comment;
import com.automifychat.app.post.Post;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    public enum Type {
        REPLY_TO_POST,      // someone commented on your post
        REPLY_TO_COMMENT,   // someone replied to your comment
        ACCEPTED_ANSWER,    // your comment was marked accepted
        MENTION             // @yourname in post or comment body
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserAccount recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private UserAccount actor;

    @Enumerated(EnumType.STRING)
    @Column(name = "notif_type", nullable = false, length = 32)
    private Type notifType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_post_id")
    private Post targetPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_comment_id")
    private Comment targetComment;

    @Column(length = 280)
    private String excerpt;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Notification() {}

    public Notification(UserAccount recipient, UserAccount actor, Type type,
                        Post targetPost, Comment targetComment, String excerpt) {
        this.recipient = recipient;
        this.actor = actor;
        this.notifType = type;
        this.targetPost = targetPost;
        this.targetComment = targetComment;
        this.excerpt = excerpt;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public UserAccount getRecipient() { return recipient; }
    public UserAccount getActor() { return actor; }
    public Type getNotifType() { return notifType; }
    public Post getTargetPost() { return targetPost; }
    public Comment getTargetComment() { return targetComment; }
    public String getExcerpt() { return excerpt; }
    public OffsetDateTime getReadAt() { return readAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public boolean isRead() { return readAt != null; }
    public void markRead() { this.readAt = OffsetDateTime.now(); }
}
