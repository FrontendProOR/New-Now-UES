package com.ues.repository;

import com.ues.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLocationIdAndDeletedFalse(Long locationId);

    List<Review> findByAuthorId(Long authorId);

    List<Review> findByLocationIdAndDeletedFalseAndHiddenFalse(Long locationId);
}
