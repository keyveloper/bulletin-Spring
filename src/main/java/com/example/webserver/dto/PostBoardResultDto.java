package com.example.webserver.dto;

import com.example.webserver.enums.PostBoardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class PostBoardResultDto {
    private PostBoardStatus postBoardStatus;
    private long id;
    private String writer;
    private LocalDateTime writingTime;
}
