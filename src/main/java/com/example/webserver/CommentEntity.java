package com.example.webserver;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Slf4j
@Table(name = "comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long commentId;

    @ManyToOne
    @JoinColumn(name="board_id")
    @JsonBackReference
    private BoardEntity board;

    @Column(name="writer")
    private String writer;

    @Column(name="writing_time")
    private LocalDateTime writingTime;

    @Column(name="text_content")
    private String textContent;
}
