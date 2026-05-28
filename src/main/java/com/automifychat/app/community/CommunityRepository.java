package com.automifychat.app.community;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Community> findAllByOrderByNameAsc();
}
