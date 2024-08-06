package com.example.webserver.controller;

import com.example.webserver.dto.PostCommentRequestDto;
import com.example.webserver.dto.PostCommentResponseDto;
import com.example.webserver.dto.PostCommentResultDto;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.enums.PostCommentStatus;
import com.example.webserver.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
        return commentService.findAllComment(boardId)
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
}