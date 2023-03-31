package com.example.footyaddicts.repos;

import com.example.footyaddicts.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post,Long > {


    boolean existsByTitle(String title);

    List<Post> findTop12ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.writtenBy = :writtenBy")
    Long countByWrittenBy(@Param("writtenBy") String writtenBy);

    @Query("SELECT p.views FROM Post p WHERE p.writtenBy = :writtenBy")
    List<Long> findViewsByWrittenBy(@Param("writtenBy") String writtenBy);

    @Query("SELECT p.views FROM Post p WHERE p.writtenBy = :writtenBy AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Long> findViewsByWrittenByAndCreatedAtBetween(@Param("writtenBy") String writtenBy, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT SUM(p.views) FROM Post p WHERE p.writtenBy = ?1 AND p.createdAt >= ?2 AND p.createdAt < ?3")
    Long findViewsByWrittenByAndCreatedAtBetween2(String writtenBy, LocalDateTime startDate, LocalDateTime endDate);

    List<Post> findTopViewedByWrittenBy(@Param("username") String username);

    @Query("SELECT p FROM Post p WHERE p.createdAt BETWEEN :oneWeekAgo AND :now ORDER BY p.views DESC")
    List<Post> findTopByViewsBetween(@Param("oneWeekAgo") LocalDateTime oneWeekAgo, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt BETWEEN :startOfDay AND :endOfDay")
    Long findCountOfPostsByCreatedAtBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    @Query("SELECT SUM(p.views) FROM Post p WHERE p.createdAt BETWEEN :startOfDay AND :endOfDay")
    Long findSumOfViewsByCreatedAtBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(p) FROM Post p")
    Long findSumOfPosts();

    @Query("SELECT SUM(p.views) FROM Post p")
    Long findSumOfViews();

    @Query("SELECT p FROM Post p WHERE p.writtenBy = :writtenBy")
    List<Post> findAllPostWrittenBy(String writtenBy);
}
