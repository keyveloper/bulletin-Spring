package com.example.webserver.controller;

import com.example.webserver.dto.PostBoardRequestDto;
import com.example.webserver.dto.PostBoardResponseDto;
import com.example.webserver.dto.PostBoardResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.service.BoardService;
import com.example.webserver.service.CommentService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/boardAll")
    public ResponseEntity<List<BoardEntity>> getAllBoardEntity() {
        return boardService.getAllBoard()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardEntity> getBoard(@PathVariable("id") Long id) {
        return boardService.findBoard(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/board")
    public ResponseEntity<PostBoardResponseDto> postBoard(@RequestBody PostBoardRequestDto request) {
        try {
            PostBoardResultDto postBoardResultDto = boardService.putBoard(
                    request.getTitle(), request.getWriter(), request.getTextContent());

            if (postBoardResultDto.getPostBoardStatus() == PostBoardStatus.Ok) {
                return ResponseEntity.ok().body(
                        PostBoardResponseDto.builder()
                                .message(PostBoardStatus.Ok.getMessage())
                                .id(postBoardResultDto.getId())
                                .writer(postBoardResultDto.getWriter())
                                .writingDateTime(postBoardResultDto.getWritingTime())
                                .build()
                );
            } else {
                return ResponseEntity.badRequest().body(
                        PostBoardResponseDto.builder()
                                .message(PostBoardStatus.Failed.getMessage())
                                .id(-1)
                                .writer(null)
                                .writingDateTime(null)
                                .build()
                );
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to save board", e);
        }
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long id) {
        return boardService.deleteBoard(id)
                .map(msg -> ResponseEntity.accepted().body(msg))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
