package com.example.webserver.service;

import com.example.webserver.dto.CustomCriteriaRequestDto;
import com.example.webserver.dto.GetCommentResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.webserver.dto.PostCommentResultDto;
import com.example.webserver.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Optional<CommentEntity> findCommentById(long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid comment id: " + id));
        return Optional.of(comment);
    }

    public Optional<List<CommentEntity>> findAllComments(long boardId) {
        List<CommentEntity> comments = commentRepository.findAllCommentsByBoardId(boardId);
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }


    @Transactional
    public PostCommentResultDto putComment(long boardId, String writer, String textContent) {
        try {
            log.info("boardId -> {}, writer -> {}, text content -> {}", boardId, writer, textContent);
            Optional<BoardEntity> board = boardRepository.findById(boardId);
            if (board.isEmpty()) {
                log.info("board is empty");
                return PostCommentResultDto.builder()
                        .boardId(-1)
                        .commentId(-1)
                        .writer(null)
                        .writingTime(null)
                        .build();
            } else {
                CommentEntity comment = CommentEntity.builder()
                        .board(board.get())
                        .writer(writer)
                        .writingTime(LocalDateTime.now())
                        .textContent(textContent)
                        .build();
                commentRepository.save(comment);
                return PostCommentResultDto.builder()
                        .postCommentStatus(PostCommentStatus.Ok)
                        .boardId(board.get().getId())
                        .commentId(comment.getId())
                        .writer(comment.getWriter())
                        .writingTime(comment.getWritingTime())
                        .build();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save comment data", e);
        }
    }

    @Transactional
    public String deleteComment(long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return "commentId: " + commentId + "was deleted";
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Comment not found with id" + commentId, 1);
        }
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

    public Optional<List<GetCommentResultDto>> getCommentByCustomCriteria(CustomCriteriaRequestDto request) {
        log.info("request -> {}", request.toString());
        if (request.getEndDate() == null) {
            request.setEndDate(LocalDateTime.now());
        }

        if (allCriteriaFieldsNotNull(request)) {
            List<CommentEntity> comments = commentRepository
                    .findCommentsByCustomCriteria(
                            request.getWriter(),
                            request.getWriterExcepted(),
                            request.getStartDate(),
                            request.getEndDate(),
                            request.getGreatThan(),
                            request.getLessThan()
                    );

            List<GetCommentResultDto> commentDtos = comments.stream()
                    .map(this::convertToGetResultDto)
                    .collect(Collectors.toList());
            return Optional.of(commentDtos);
        } else {

            return Optional.empty();
        }
    }

    private GetCommentResultDto convertToGetResultDto(CommentEntity comment) {
        return GetCommentResultDto.builder()
                .boardId(comment.getBoard().getId())
                .commentId(comment.getId())
                .writer(comment.getWriter())
                .textContent(comment.getTextContent())
                .writingTime(comment.getWritingTime())
                .build();
    }

    private boolean allCriteriaFieldsNotNull(CustomCriteriaRequestDto request) {
        return request.getWriter() != null &&
                request.getWriterExcepted() != null &&
                request.getStartDate() != null &&
                request.getEndDate() != null &&
                request.getLessThan() != null &&
                request.getGreatThan() != null;
    }
}