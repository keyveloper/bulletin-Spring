package com.example.webserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class PostBoardRequestDto {
    private final String title;
    private final String writer;
    private final String textContent;
}
