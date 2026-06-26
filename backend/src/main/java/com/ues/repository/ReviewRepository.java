package com.ues.repository;

import com.ues.model.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLocationIdAndDeletedFalse(Long locationId);

    List<Review> findByLocationIdAndDeletedFalse(Long locationId, Sort sort);

    List<Review> findByAuthorId(Long authorId);

    List<Review> findByLocationIdAndDeletedFalseAndHiddenFalse(Long locationId);

    List<Review> findByLocationIdAndDeletedFalseAndHiddenFalse(Long locationId, Sort sort);

    @Query("SELECT AVG(" +
           "  (COALESCE(r.rate.performance, 0) + COALESCE(r.rate.soundAndLighting, 0) + " +
           "   COALESCE(r.rate.venue, 0) + COALESCE(r.rate.overallImpression, 0)) * 1.0 / " +
           "  (CASE WHEN r.rate.performance IS NOT NULL THEN 1 ELSE 0 END + " +
           "   CASE WHEN r.rate.soundAndLighting IS NOT NULL THEN 1 ELSE 0 END + " +
           "   CASE WHEN r.rate.venue IS NOT NULL THEN 1 ELSE 0 END + " +
           "   CASE WHEN r.rate.overallImpression IS NOT NULL THEN 1 ELSE 0 END)" +
           ") FROM Review r WHERE r.location.id = :locationId AND r.deleted = false " +
           "AND (r.rate.performance IS NOT NULL OR r.rate.soundAndLighting IS NOT NULL " +
           "OR r.rate.venue IS NOT NULL OR r.rate.overallImpression IS NOT NULL)")
    Double calculateAverageRating(@Param("locationId") Long locationId);

    List<Review> findTop3ByLocationIdAndDeletedFalseOrderByCreatedAtDesc(Long locationId);
}
