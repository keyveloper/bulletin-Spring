package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class GetCommentResultDto {
    private final long boardId;
    private final long commentId;
    private final String writer;
    private final String textContent;
    private final LocalDateTime writingTime;
}
