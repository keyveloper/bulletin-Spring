package com.example.webserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostBoardResponseDto {
    private final String message;
    private long id;
    private String writer;
    private LocalDateTime writingDateTime;
}
