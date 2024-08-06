package com.example.webserver.dto;

import com.example.webserver.enums.PostCommentStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostCommentRequestDto {
    private final long boardId;
    private final String writer;
    private final String textContent;
}
