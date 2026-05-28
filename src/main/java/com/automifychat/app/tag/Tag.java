package com.automifychat.app.tag;

import com.automifychat.app.community.Community;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = {"community_id", "name"}))
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Tag() {}

    public Tag(Community community, String name) {
        this.community = community;
        this.name = name;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public Community getCommunity() { return community; }
    public String getName() { return name; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
