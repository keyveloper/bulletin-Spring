package com.example.webserver.service;

import com.example.webserver.dto.CommentResponse;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Optional<CommentEntity> findCommentById(long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid comment id: " + id));
        return Optional.of(comment);
    }

    public Optional<List<CommentEntity>> findAllComments(long boardId) {
        List<CommentEntity> comments = commentRepository.findAllCommentsByBoardID(boardId);
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }

    @Transactional
    public Optional<CommentResponse> putComment(long boardId, String writer, String textContent) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + boardId));
        CommentEntity comment = CommentEntity.builder()
                .board(board)
                .writer(writer)
                .writingTime(LocalDateTime.now())
                .textContent(textContent)
                .build();
        commentRepository.save(comment);

        CommentResponse response = CommentResponse.builder()
                .commentEntity(comment).message("comment added successfully").build();
        return Optional.of(response);
    }

    @Transactional
    public Optional<CommentResponse> deleteComment(long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        CommentResponse response = CommentResponse.builder()
                .commentEntity(comment)
                .message("comment deleted successfully")
                .build();
        commentRepository.deleteById(commentId);
        return Optional.of(response);
    }

    // find Comments by username
    public Optional<List<CommentEntity>> getCommentsByWriter(String writer) {
        return Optional.of(commentRepository.findByBoardWriterNameJpql(writer));
    }

    // find Comments by not username
    public Optional<List<CommentEntity>> getCommentsByWriterNot(String writer) {
        return Optional.of(commentRepository.findByBoardWriterNotJpql(writer));
    }

    // find Comment by date range
    public Optional<List<CommentEntity>> getCommentsByCriteriaDate(LocalDateTime startDate, LocalDateTime endDate) {
        return Optional.of(commentRepository.findCommentWithBoardDateRange(startDate, endDate));
    }

    public Optional<List<CommentEntity>> getCommentsByCriteria(
            Integer underPivot, Integer upperPivot,
            boolean underEqual, boolean upperEqual, boolean between
    ) {
        if (underPivot != null && upperPivot == null) {
            if (underEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountLessThanEqual(underPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountLessThan(underPivot));
            }
        } else if (underPivot == null && upperPivot != null) {
            if (upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountGreaterThanEqual(upperPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountGreaterThan(upperPivot));
            }
        } else if (between && underPivot != null && upperPivot != null) {
            if (underEqual && upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenBothEqual(underPivot, upperPivot));
            } else if (underEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenUnderEqual(underPivot, upperPivot));
            } else if (upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenUpperEqual(underPivot, upperPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetween(underPivot, upperPivot));
            }
        }
        return Optional.empty();
    }
}
