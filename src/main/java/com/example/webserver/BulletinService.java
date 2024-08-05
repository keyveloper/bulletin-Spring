package com.example.webserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulletinService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Optional<List<BoardEntity>> getAllBoard() {
        List<BoardEntity> boards = boardRepository.findAll();
        return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
    }

    public Optional<Long> putBoard(String title, String writer, String textContent) {
        BoardEntity board = BoardEntity.builder()
                .title(title).writer(writer)
                .textContent(textContent)
                .writingDate(LocalDateTime.now())
                .readingCount(0)
                .build();
        boardRepository.save(board);
        return Optional.of(board.getBoardId());
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
            return Optional.of("count added successfully " + board.getBoardId());
        } else {
            return Optional.empty();
        }
    }


}
