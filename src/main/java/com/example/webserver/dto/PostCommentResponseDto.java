package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class PostCommentResponseDto {
    private final long boardId;
    private final long commentId;
    private final String writer;
    private final LocalDateTime writingTime;
}
