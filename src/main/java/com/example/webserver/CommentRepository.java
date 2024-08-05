package com.example.webserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT c FROM CommentEntity c WHERE c.board.boardId = :boardId")
    List<CommentEntity> findAllCommentsByBoardID(@Param("boardId") Long boardId);

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

    // board reading count : search under
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.board.readingCount < :pivot")
    List<CommentEntity> findCommentsWithBoardReadingCountLessThan(@Param("pivot") Integer pivot);

    @Query("SELECT c FROM CommentEntity c WHERE c.board.readingCount > :pivot")
    List<CommentEntity> findCommentsWithBoardReadingCountGreaterThan(@Param("pivot") Integer pivot);

    @Query("SELECT c FROM CommentEntity c WHERE c.board.readingCount <= :pivot")
    List<CommentEntity> findCommentsWithBoardReadingCountLessThanEqual(@Param("pivot") Integer pivot);

    @Query("SELECT c FROM CommentEntity c WHERE c.board.readingCount >= :pivot")
    List<CommentEntity> findCommentsWithBoardReadingCountGreaterThanEqual(@Param("pivot") Integer pivot);

    @Query("SELECT c FROM CommentEntity c WHERE :underPivot < c.board.readingCount " +
            "and c.board.readingCount < :upperPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetween(@Param("underPivot") Integer underPivot,
                                                                @Param("upperPivot") Integer upperPivot);

    @Query("SELECT c FROM CommentEntity c WHERE :underPivot <= c.board.readingCount " +
            "and c.board.readingCount < :upperPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetweenUnderEqual(@Param("underPivot") Integer underPivot,
                                                                     @Param("upperPivot") Integer upperPivot);
    @Query("SELECT c FROM CommentEntity c WHERE :underPivot < c.board.readingCount " +
            "and c.board.readingCount <= :upeerPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetweenUpperEqual(@Param("underPivot") Integer underPivot,
                                                                          @Param("upperPivot") Integer upperPivot);

    @Query("SELECT c FROM CommentEntity c WHERE :underPivot <= c.board.readingCount " +
            "and c.board.readingCount <= :upperPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetweenBothEqual(@Param("underPivot") Integer underPivot,
                                                                          @Param("upperPivot") Integer upperPivot);
}
