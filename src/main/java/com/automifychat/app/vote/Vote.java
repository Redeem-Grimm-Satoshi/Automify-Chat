package com.automifychat.app.vote;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "votes")
@IdClass(Vote.Key.class)
public class Vote {

    public enum TargetType { POST, COMMENT }

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", length = 16)
    private TargetType targetType;

    @Id
    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "vote_value", nullable = false)
    private short value;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Vote() {}

    public Vote(Long userId, TargetType targetType, Long targetId, short value) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.value = value;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getUserId() { return userId; }
    public TargetType getTargetType() { return targetType; }
    public Long getTargetId() { return targetId; }
    public short getValue() { return value; }
    public void setValue(short value) { this.value = value; }

    public static class Key implements Serializable {
        private Long userId;
        private TargetType targetType;
        private Long targetId;

        public Key() {}
        public Key(Long userId, TargetType targetType, Long targetId) {
            this.userId = userId; this.targetType = targetType; this.targetId = targetId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key k)) return false;
            return Objects.equals(userId, k.userId)
                    && targetType == k.targetType
                    && Objects.equals(targetId, k.targetId);
        }
        @Override
        public int hashCode() { return Objects.hash(userId, targetType, targetId); }
    }
}
