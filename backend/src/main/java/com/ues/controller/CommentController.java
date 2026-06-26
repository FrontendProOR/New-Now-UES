package com.ues.controller;

import com.ues.dto.CommentDto;
import com.ues.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<CommentDto> createRootComment(@PathVariable Long reviewId,
                                                         @RequestBody Map<String, String> body,
                                                         Authentication authentication) {
        String text = body.get("text");
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }
        String email = authentication.getName();
        logger.info("User {} creating comment on review id={}", email, reviewId);
        return ResponseEntity.ok(commentService.createRootComment(reviewId, text, email));
    }

    @PostMapping("/api/comments/{commentId}/replies")
    public ResponseEntity<CommentDto> createReply(@PathVariable Long commentId,
                                                   @RequestBody Map<String, String> body,
                                                   Authentication authentication) {
        String text = body.get("text");
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Reply text cannot be empty");
        }
        String email = authentication.getName();
        logger.info("User {} replying to comment id={}", email, commentId);
        return ResponseEntity.ok(commentService.createReply(commentId, text, email));
    }

    @GetMapping("/api/reviews/{reviewId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentTree(@PathVariable Long reviewId) {
        logger.debug("Fetching comment tree for review id={}", reviewId);
        return ResponseEntity.ok(commentService.getCommentTree(reviewId));
    }
}
