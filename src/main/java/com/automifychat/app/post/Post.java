package com.automifychat.app.post;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.community.Community;
import com.automifychat.app.tag.Tag;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "body_md", nullable = false, columnDefinition = "text")
    private String bodyMd;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false, length = 16)
    private PostType postType;

    @Column(nullable = false)
    private int score;

    @Column(name = "accepted_answer_comment_id")
    private Long acceptedAnswerCommentId;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    protected Post() {}

    public Post(Community community, UserAccount author, String title, String bodyMd, PostType postType) {
        this.community = community;
        this.author = author;
        this.title = title;
        this.bodyMd = bodyMd;
        this.postType = postType;
        this.score = 0;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Community getCommunity() { return community; }
    public UserAccount getAuthor() { return author; }
    public String getTitle() { return title; }
    public String getBodyMd() { return bodyMd; }
    public PostType getPostType() { return postType; }
    public int getScore() { return score; }
    public Long getAcceptedAnswerCommentId() { return acceptedAnswerCommentId; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Set<Tag> getTags() { return tags; }

    public boolean isDeleted() { return deletedAt != null; }

    public void addScore(int delta) { this.score += delta; }
    public void setAcceptedAnswerCommentId(Long commentId) { this.acceptedAnswerCommentId = commentId; }
    public void markDeleted() { this.deletedAt = OffsetDateTime.now(); }
}
