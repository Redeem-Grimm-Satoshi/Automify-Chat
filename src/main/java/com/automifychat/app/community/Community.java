package com.automifychat.app.community;

import com.automifychat.app.auth.UserAccount;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "communities")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String slug;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 280)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserAccount owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Community() {}

    public Community(String slug, String name, String description, UserAccount owner) {
        this.slug = slug;
        this.name = name;
        this.description = description == null ? "" : description;
        this.owner = owner;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public UserAccount getOwner() { return owner; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
