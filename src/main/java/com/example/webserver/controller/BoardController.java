package com.example.webserver.controller;

import com.example.webserver.dto.*;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.enums.UpdateStatus;
import com.example.webserver.service.BoardService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<List<GetBoardResponseDto>> getAllBoardEntity() {
        List<GetBoardResultDto> resultDtos = boardService.findAll();
        return ResponseEntity.ok(resultDtos.stream()
                .map(this::convertGetResultToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<GetBoardResponseDto> getBoard(@PathVariable("id") Long id) {
        Optional<GetBoardResultDto> resultDto = boardService.findById(id);
        return resultDto.map(getBoardResultDto -> ResponseEntity.ok(convertGetResultToResponse(getBoardResultDto)))
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

    // update
    @PostMapping("/board/update")
    public ResponseEntity<String> updateBoardById(
            @RequestParam long id,
            @RequestParam(required = false) String textContent,
            @RequestParam(required = false) String writer,
            @RequestParam(required = false) String title
    ) {
        UpdateResultDto resultDto = boardService.updateBoard(UpdateRequestDto.builder()
                .id(id)
                .textContent(textContent)
                .writer(writer)
                .title(title)
                .build());

        if (resultDto.getUpdateStatus() == UpdateStatus.Ok) {
            return ResponseEntity.ok().body(resultDto.getUpdateStatus().getMessage());
        } else {
            return ResponseEntity.badRequest().body(resultDto.getUpdateStatus().getMessage());
        }
    }

    @GetMapping("/boards/like")
    public ResponseEntity<List<GetBoardResponseDto>> getByWriterOrContentLike(
            @RequestParam (value = "writer", required = false) String writer,
            @RequestParam(value = "textContent", required = false) String textContent) {
        List<GetBoardResultDto> resultDtos = boardService.findByWriterOrContentLike(writer, textContent);
        return ResponseEntity.ok(resultDtos.stream()
                .map(this::convertGetResultToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/boards/dsl/like")
    public ResponseEntity<List<GetBoardResponseDto>> getByWriterLikeDsl(
            @RequestParam String writer
    ) {
        List<GetBoardResultDto> resultDtos = boardService.findByWriterLikeDsl(writer);
        return ResponseEntity.ok(resultDtos
                .stream()
                .map(this::convertGetResultToResponse)
                .collect(Collectors.toList()));
    }

    private GetBoardResponseDto convertGetResultToResponse(GetBoardResultDto resultDto) {
        return GetBoardResponseDto.builder()
                .id(resultDto.getId())
                .title(resultDto.getTitle())
                .writer(resultDto.getWriter())
                .writingTime(resultDto.getWritingTime())
                .lastModifiedTime(resultDto.getLastModifiedTime())
                .readingCount(resultDto.getReadingCount())
                .textContent(resultDto.getTextContent())
                .comments(resultDto.getComments())
                .build();
    }

}
