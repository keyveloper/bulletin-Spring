package com.example.webserver.repository;

import com.example.webserver.dto.GetBoardCriteriaRequest;
import com.example.webserver.entity.BoardEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BoardCriteriaRepository {
    // find by board writer name
    List<BoardEntity> findByContainingWriter(String writer);
    // find by board textContent
    List<BoardEntity> findByContainingTextContent(String textContent);
    // find by both
    List<BoardEntity> findByContainingWriterAndText(String writer, String textContent);
    // find by comment writer name
    List<BoardEntity> findByContainingCommentWriter(String writer);
    // find by comment writer content
    List<BoardEntity> findByContainingCommentContent(String textContent);
    // find by containing comment both
    List<BoardEntity> findByContainingCommentWriterAndText(String writer, String textContent);

}