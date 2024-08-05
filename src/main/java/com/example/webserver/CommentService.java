package com.example.webserver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Optional<List<CommentEntity>> getCommentsByCriteria(
            Integer underPivot, Integer upperPivot,
            boolean underEqual, boolean upperEqual, boolean between
    ) {
        if (underPivot != null && upperPivot == null) {
            if (underEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountLessThanEqual(underPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountLessThan(underPivot));
            }
        } else if (underPivot == null && upperPivot != null) {
            if (upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountGreaterThanEqual(upperPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountGreaterThan(upperPivot));
            }
        } else if (between && underPivot != null && upperPivot != null) {
            if (underEqual && upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenBothEqual(underPivot, upperPivot));
            } else if (underEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenUnderEqual(underPivot, upperPivot));
            } else if (upperEqual) {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetweenUpperEqual(underPivot, upperPivot));
            } else {
                return Optional.of(commentRepository.findCommentsWithBoardReadingCountBetween(underPivot, upperPivot));
            }
        }
        return Optional.empty();
    }
}
