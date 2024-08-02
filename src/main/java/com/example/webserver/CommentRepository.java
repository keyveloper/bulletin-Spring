package com.example.webserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // find by username
    @Query(value = "SELECT c From CommentEntity c Where c.writer = :writer")
    List<CommentEntity> findByWriterNameJpql(@Param("writer") String writer);
    // find by except username
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.writer != :writer")
    List<CommentEntity> findByWriterNotJpql(@Param("writer") String writer);

    //criteria: 3 <= date < 10 (pivot = today)
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.writingTime BETWEEN :startDate AND :endDate")
    List<CommentEntity> findCommentWithDateRange(@Param("startDate")LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    // board reading count : search comment
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.board.readingCount < 100 " +
            "or 10000 < c.board.readingCount ")
    List<CommentEntity> findByBoardReadingCount();

    List<CommentEntity> findByBoardBoardId(long bardId);
}
