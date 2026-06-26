package com.ues.controller;

import com.ues.dto.ReviewDto;
import com.ues.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
public class ManagerReviewController {

    private static final Logger logger = LogManager.getLogger(ManagerReviewController.class);

    private final ReviewService reviewService;

    public ManagerReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/locations/{locationId}/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews(
            @PathVariable Long locationId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        logger.info("Manager fetching all reviews for location id={}", locationId);
        return ResponseEntity.ok(reviewService.getAllReviewsForLocation(locationId, sortBy, direction));
    }

    @PutMapping("/reviews/{id}/hide")
    public ResponseEntity<ReviewDto> hideReview(@PathVariable Long id) {
        logger.info("Manager hiding review id={}", id);
        return ResponseEntity.ok(reviewService.hideReview(id));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        logger.info("Manager logically deleting review id={}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
