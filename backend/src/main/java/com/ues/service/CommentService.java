package com.ues.service;

import com.ues.dto.CommentDto;
import com.ues.model.Comment;
import com.ues.model.Review;
import com.ues.model.User;
import com.ues.repository.CommentRepository;
import com.ues.repository.ManagesRepository;
import com.ues.repository.ReviewRepository;
import com.ues.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LogManager.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ManagesRepository managesRepository;

    public CommentService(CommentRepository commentRepository,
                          ReviewRepository reviewRepository,
                          UserRepository userRepository,
                          ManagesRepository managesRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.managesRepository = managesRepository;
    }

    @Transactional
    public CommentDto createRootComment(Long reviewId, String text, String userEmail) {
        User author = findUser(userEmail);
        Review review = findReview(reviewId);

        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setReview(review);

        Comment saved = commentRepository.save(comment);
        logger.info("User {} created root comment on review id={}", userEmail, reviewId);
        return toDto(saved);
    }

    @Transactional
    public CommentDto createReply(Long parentId, String text, String userEmail) {
        User author = findUser(userEmail);
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Review review = parent.getReview();
        Long locationId = review.getLocation().getId();

        boolean authorIsManager = isManagerOfLocation(author.getId(), locationId)
                || "ROLE_ADMIN".equals(author.getRole());

        if (!authorIsManager) {
            // Običan korisnik sme odgovoriti samo na komentar menadžera
            boolean parentIsFromManager = isManagerOfLocation(parent.getAuthor().getId(), locationId)
                    || "ROLE_ADMIN".equals(parent.getAuthor().getRole());

            if (!parentIsFromManager) {
                throw new IllegalStateException(
                        "Regular users can only reply to comments from managers");
            }
        }

        Comment reply = new Comment();
        reply.setText(text);
        reply.setAuthor(author);
        reply.setReview(review);
        reply.setParent(parent);

        Comment saved = commentRepository.save(reply);
        logger.info("User {} replied to comment id={} on review id={}", userEmail, parentId, review.getId());
        return toDto(saved);
    }

    public List<CommentDto> getCommentTree(Long reviewId) {
        List<Comment> roots = commentRepository.findByReviewIdAndParentIsNull(reviewId);
        return roots.stream()
                .map(this::toDtoWithReplies)
                .toList();
    }

    private boolean isManagerOfLocation(Long userId, Long locationId) {
        return managesRepository.existsByUserIdAndLocationIdAndEndDateIsNull(userId, locationId);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    private CommentDto toDtoWithReplies(Comment comment) {
        CommentDto dto = toDto(comment);
        if (comment.getReplies() != null) {
            dto.setReplies(comment.getReplies().stream()
                    .map(this::toDtoWithReplies)
                    .toList());
        }
        return dto;
    }

    private CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setAuthorRole(comment.getAuthor().getRole());
        dto.setReviewId(comment.getReview().getId());
        dto.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        return dto;
    }
}
