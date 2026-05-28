package com.automifychat.app.vote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Vote.Key> {
    Optional<Vote> findByUserIdAndTargetTypeAndTargetId(Long userId, Vote.TargetType type, Long targetId);
}
