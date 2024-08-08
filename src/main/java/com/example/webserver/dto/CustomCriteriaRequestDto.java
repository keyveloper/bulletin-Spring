package com.example.webserver.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CustomCriteriaRequestDto {
    private final String writer;
    private final String writerExcepted;
    private final LocalDateTime startDate;
    private LocalDateTime endDate;
    private final Integer greatThan;
    private final Integer lessThan;
}
