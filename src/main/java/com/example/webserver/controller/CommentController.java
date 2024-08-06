package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentEntity> findCommentById(@PathVariable Long id) {
        return commentService.findCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/commentAll/{boardId}")
    public ResponseEntity<List<CommentEntity>> findAllComment(@PathVariable Long boardId) {
        return commentService.findAllComments(boardId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<PostCommentResponseDto> postComment(@RequestBody PostCommentRequestDto request) {
        log.info("request: {}", request);
        PostCommentResultDto commentResultDto = commentService.putComment(
                request.getBoardId(), request.getWriter(), request.getTextContent());
        // status별로 Response 구분
        if (commentResultDto.getPostCommentStatus() == PostCommentStatus.Ok) {
            return ResponseEntity.ok().body(
                    PostCommentResponseDto.builder()
                            .boardId(commentResultDto.getBoardId())
                            .commentId(commentResultDto.getCommentId())
                            .writer(commentResultDto.getWriter())
                            .writingTime(commentResultDto.getWritingTime())
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    PostCommentResponseDto.builder()
                            .boardId(-1)
                            .commentId(-1)
                            .writer(null)
                            .writingTime(null)
                            .build()
            );
        }
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        try {
            String commentDeleteMessage = commentService.deleteComment(id);
            return ResponseEntity.ok().body(commentDeleteMessage);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("can not delete comment");
        }
    }

    @GetMapping("/comment/custom_criteria")
    public ResponseEntity<List<GetCommentResultDto>> getCommentsByCustomCriteria(
            @RequestParam String writer,
            @RequestParam String writerExcepted,
            @RequestParam LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam Integer lessPivot,
            @RequestParam Integer greaterPivot) {
        Optional<List<GetCommentResultDto>> comments =
                commentService.getCommentByCustomCriteria(
                CustomCriteriaRequestDto.builder()
                        .writer(writer)
                        .writerExcepted(writerExcepted)
                        .startDate(startDate)
                        .endDate(endDate)
                        .lessPivot(lessPivot)
                        .greatPivot(greaterPivot)
                        .build());
        return comments.map(getCommentResultDtos ->
                        ResponseEntity.ok().body(getCommentResultDtos))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}