package com.example.webserver.service;

import com.example.webserver.dto.PostBoardResultDto;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.enums.PostBoardStatus;
import com.example.webserver.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Optional<List<BoardEntity>> getAllBoard() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
    }

    public PostBoardResultDto putBoard(String title, String writer, String textContent) {
        if (title != null && writer != null && textContent != null) {
            BoardEntity board = BoardEntity.builder()
                    .title(title).writer(writer)
                    .textContent(textContent)
                    .writingTime(LocalDateTime.now())
                    .readingCount(0)
                    .build();
            try {
                boardRepository.save(board);
                return PostBoardResultDto.builder()
                        .postBoardStatus(PostBoardStatus.Ok)
                        .id(board.getId()).writer(board.getWriter())
                        .writingTime(board.getWritingTime()).build();
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to save board", e);
            }
        }

        return PostBoardResultDto.builder()
                .postBoardStatus(PostBoardStatus.Failed)
                .id(-1)
                .writer(null)
                .writingTime(null)
                .build();
    }

    public Optional<BoardEntity> findBoard(long id) {
        return boardRepository.findById(id);
    }

    public Optional<String> deleteBoard(Long id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
            return Optional.of("\"Board Successfully deleted!! board id: \" + id");
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<String> updateBoard(long id, String content) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setTextContent(content);
            boardRepository.save(board);
            return Optional.of("new content updated successfully " + id);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<String> addReadingCount(long id) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(id);
        if (boardOpt.isPresent()) {
            BoardEntity board = boardOpt.get();
            board.setReadingCount(board.getReadingCount() + 1);
            boardRepository.save(board);
            return Optional.of("count added successfully " + board.getId());
        } else {
            return Optional.empty();
        }
    }
}
