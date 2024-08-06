package com.example.webserver.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostCommentStatus {
    Ok("comment saved successfully"),
    Failed("comment saving failed, something happend wrong");
    private final String message;
}
