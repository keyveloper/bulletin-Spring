package com.example.webserver.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostBoardStatus {
    Ok("board saved successfully"),
    Failed("board saving failed, something happened wrong...");

    private final String message;
}
