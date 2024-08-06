package com.example.webserver.repository;

import com.example.webserver.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT c FROM CommentEntity c WHERE c.board.boardId = :boardId")
    List<CommentEntity> findAllCommentsByBoardID(@Param("boardId") Long boardId);

    // find by /board-writer
    @Query(value = "SELECT c From CommentEntity c Where c.board.writer = :writer")
    List<CommentEntity> findByBoardWriterNameJpql(@Param("writer") String writer);
    // find by /board-writer except
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.board.writer != :writer")
    List<CommentEntity> findByBoardWriterNotJpql(@Param("writer") String writer);

    // find by /board-writing-date
    //criteria: 3 <= date < 10 (pivot = today)
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.board.writingDate BETWEEN :startDate AND :endDate")
    List<CommentEntity> findCommentWithBoardDateRange(@Param("startDate")LocalDateTime startDate,
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
            "and c.board.readingCount <= :upperPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetweenUpperEqual(@Param("underPivot") Integer underPivot,
                                                                          @Param("upperPivot") Integer upperPivot);

    @Query("SELECT c FROM CommentEntity c WHERE :underPivot <= c.board.readingCount " +
            "and c.board.readingCount <= :upperPivot")
    List<CommentEntity> findCommentsWithBoardReadingCountBetweenBothEqual(@Param("underPivot") Integer underPivot,
                                                                          @Param("upperPivot") Integer upperPivot);
}
