package com.automifychat.app.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByCommunityIdAndName(Long communityId, String name);
    List<Tag> findByCommunityIdOrderByNameAsc(Long communityId);
}
