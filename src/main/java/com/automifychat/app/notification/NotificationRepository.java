package com.automifychat.app.notification;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @EntityGraph(attributePaths = {"actor", "targetPost", "targetComment"})
    @Query("select n from Notification n where n.recipient.id = :userId order by n.createdAt desc")
    List<Notification> findRecentForUser(@Param("userId") Long userId);

    @Query("select count(n) from Notification n where n.recipient.id = :userId and n.readAt is null")
    long countUnreadForUser(@Param("userId") Long userId);
}
