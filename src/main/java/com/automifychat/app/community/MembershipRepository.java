package com.automifychat.app.community;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Membership.Key> {
    boolean existsByUserIdAndCommunityId(Long userId, Long communityId);
}
