package com.automifychat.app.community;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "memberships")
@IdClass(Membership.Key.class)
public class Membership {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "community_id")
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;

    public enum Role { MEMBER, MOD, OWNER }

    protected Membership() {}

    public Membership(Long userId, Long communityId, Role role) {
        this.userId = userId;
        this.communityId = communityId;
        this.role = role;
        this.joinedAt = OffsetDateTime.now();
    }

    public Long getUserId() { return userId; }
    public Long getCommunityId() { return communityId; }
    public Role getRole() { return role; }
    public OffsetDateTime getJoinedAt() { return joinedAt; }

    public static class Key implements Serializable {
        private Long userId;
        private Long communityId;

        public Key() {}
        public Key(Long userId, Long communityId) { this.userId = userId; this.communityId = communityId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key k)) return false;
            return Objects.equals(userId, k.userId) && Objects.equals(communityId, k.communityId);
        }
        @Override
        public int hashCode() { return Objects.hash(userId, communityId); }
    }
}
