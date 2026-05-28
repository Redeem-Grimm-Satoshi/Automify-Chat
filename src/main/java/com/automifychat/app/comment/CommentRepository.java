package com.automifychat.app.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "author")
    @Query("select c from Comment c where c.post.id = :postId order by c.score desc, c.createdAt asc")
    List<Comment> findForPost(@Param("postId") Long postId);

    @EntityGraph(attributePaths = {"author", "post"})
    @Query("select c from Comment c where c.author.username = :username and c.deletedAt is null order by c.createdAt desc")
    List<Comment> findByAuthor(@Param("username") String username);

    @EntityGraph(attributePaths = {"author", "post"})
    @Query("select c from Comment c where c.deletedAt is null and lower(c.bodyMd) like lower(concat('%', :q, '%')) order by c.createdAt desc")
    List<Comment> searchByText(@Param("q") String query);
}
