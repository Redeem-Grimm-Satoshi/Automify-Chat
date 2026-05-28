package com.automifychat.app.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.deletedAt is null order by p.createdAt desc")
    List<Post> findRecentFeed(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.deletedAt is null order by p.score desc, p.createdAt desc")
    List<Post> findTopFeed(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.community.id = :communityId and p.deletedAt is null order by p.createdAt desc")
    List<Post> findRecentForCommunity(@Param("communityId") Long communityId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.community.id = :communityId and p.deletedAt is null order by p.score desc, p.createdAt desc")
    List<Post> findTopForCommunity(@Param("communityId") Long communityId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community", "tags"})
    @Query("select distinct p from Post p join p.tags t where p.community.id = :communityId and t.name = :tag and p.deletedAt is null order by p.createdAt desc")
    List<Post> findByCommunityAndTag(@Param("communityId") Long communityId, @Param("tag") String tag, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community", "tags"})
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findByIdWithRelations(@Param("id") Long id);

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.author.username = :username and p.deletedAt is null order by p.createdAt desc")
    List<Post> findByAuthor(@Param("username") String username, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "community"})
    @Query("select p from Post p where p.deletedAt is null and (lower(p.title) like lower(concat('%', :q, '%')) or lower(p.bodyMd) like lower(concat('%', :q, '%'))) order by p.createdAt desc")
    List<Post> searchByText(@Param("q") String query, Pageable pageable);
}
