package com.example.webserver.dto;

import lombok.Data;

@Data
public class RequestBoard {
    private String title;
    private String writer;
    private String content;
}
