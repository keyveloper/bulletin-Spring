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

    public Optional<CommentEntity> findCommentById(long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid comment id: " + id));
        return Optional.of(comment);
    }

    public Optional<List<CommentEntity>> findAllComment(long boardId) {
        List<CommentEntity> comments = commentRepository.findByBoardBoardId(boardId);
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }

    @Transactional
    public Optional<CommentResponse> putComment(long boardId, String writer, String textContent) {
         BoardEntity board = boardRepository.findById(boardId)
                 .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + boardId));
         CommentEntity comment = CommentEntity.builder()
                .board(board)
                .writer(writer)
                .writingTime(LocalDateTime.now())
                .textContent(textContent)
                .build();
        commentRepository.save(comment);

        CommentResponse response = CommentResponse.builder()
                .commentEntity(comment).message("comment added successfully").build();
        return Optional.of(response);
    }

    @Transactional
    public Optional<CommentResponse> deleteComment(long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        CommentResponse response = CommentResponse.builder()
                .commentEntity(comment)
                .message("comment deleted successfully")
                .build();
        commentRepository.deleteById(commentId);
        return Optional.of(response);
    }
}
