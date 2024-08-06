package com.example.webserver.dto;

import com.example.webserver.enums.PostCommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class PostCommentResultDto {
    PostCommentStatus postCommentStatus;
    long boardId;
    long commentId;
    String writer;
    LocalDateTime writingTime;
}
