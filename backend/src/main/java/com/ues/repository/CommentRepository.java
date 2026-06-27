package com.ues.repository;

import com.ues.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReviewIdAndParentIsNull(Long reviewId);

    List<Comment> findByReviewId(Long reviewId);

    List<Comment> findByReviewIdAndParentIsNotNull(Long reviewId);
}
