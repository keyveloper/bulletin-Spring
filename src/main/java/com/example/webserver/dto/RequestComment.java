package com.example.webserver.dto;

import lombok.Data;

@Data
public class RequestComment {
    long boardId;
    String writer;
    String textContent;
}
