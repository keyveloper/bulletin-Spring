package com.example.webserver;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class DataController {
    private final BulletinService bulletinService;
    private final CommentService commentService;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        return bulletinService.getAllBoard()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        return bulletinService.findBoard(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<Object> postBoard(@RequestBody RequestBoard request) {
        return bulletinService.putBoard(request.getTitle(), request.getWriter(), request.getContent())
                .map(id -> ResponseEntity.created(URI.create("http://localhost:8080/board/" + id)).build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        return bulletinService.deleteBoard(id)
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentEntity> findCommentById(@PathVariable Long id) {
        return bulletinService.findCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/commentAll/{boardId}")
    public ResponseEntity<List<CommentEntity>> findAllComment(@PathVariable Long boardId) {
        return bulletinService.findAllComments(boardId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> postComment(@RequestBody RequestComment request) {
        log.info("request: {}", request);
        return bulletinService.putComment(request.getBoardId(), request.getWriter(), request.getTextContent())
                .map(response -> ResponseEntity.created(URI.create("http://localhost:8080/comment/"
                        + response.getCommentEntity().getCommentId())).body(response))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id) {
        return bulletinService.deleteComment(id)
                .map(response -> ResponseEntity.accepted().body(response))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // logics
    @GetMapping("/comments/by-board-writer")
    public ResponseEntity<List<CommentEntity>> getCommentByWriter(
            @RequestParam String writer,
            @RequestParam(required = false, defaultValue = "false") Boolean exception) {
        if (exception) {
            return bulletinService.getCommentsByWriterNot(writer)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        log.info("writer -> {}", writer);
        log.info("exception -> {]", exception);
        return bulletinService.getCommentsByWriter(writer)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/comments/by-board-writing-date-range")
    public ResponseEntity<List<CommentEntity>> getCommentByDateRAnge(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return bulletinService.getCommentsByCriteriaDate(startDate, endDate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/comments/by-board-reading-criteria")
    public ResponseEntity<List<CommentEntity>> getCommentByBoardReadingCriteria(
            @RequestParam(required = false) int underPivot,
            @RequestParam(required = false) int upperPivot,
            @RequestParam(defaultValue = "false") boolean underEqual,
            @RequestParam(defaultValue = "false") boolean upperEqual,
            @RequestParam(defaultValue = "false") Boolean between
    ) {
        Optional<List<CommentEntity>> comments = commentService.getCommentsByCriteria(
                underPivot, upperPivot, underEqual, upperEqual, between);
            return comments.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
