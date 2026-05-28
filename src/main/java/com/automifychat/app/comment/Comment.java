package com.automifychat.app.comment;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.post.Post;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "body_md", nullable = false, columnDefinition = "text")
    private String bodyMd;

    @Column(nullable = false)
    private int score;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Comment() {}

    public Comment(Post post, UserAccount author, Long parentCommentId, String bodyMd) {
        this.post = post;
        this.author = author;
        this.parentCommentId = parentCommentId;
        this.bodyMd = bodyMd;
        this.score = 0;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public UserAccount getAuthor() { return author; }
    public Long getParentCommentId() { return parentCommentId; }
    public String getBodyMd() { return bodyMd; }
    public int getScore() { return score; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public boolean isDeleted() { return deletedAt != null; }

    public void addScore(int delta) { this.score += delta; }
    public void markDeleted() { this.deletedAt = OffsetDateTime.now(); }
}
