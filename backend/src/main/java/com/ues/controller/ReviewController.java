package com.ues.controller;

import com.ues.dto.CreateReviewRequest;
import com.ues.dto.ReviewDto;
import com.ues.service.ReviewService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations/{locationId}/reviews")
public class ReviewController {

    private static final Logger logger = LogManager.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@PathVariable Long locationId,
                                                   @Valid @RequestBody CreateReviewRequest request,
                                                   Authentication authentication) {
        String email = authentication.getName();
        logger.info("User {} creating review for location id={}", email, locationId);
        ReviewDto dto = reviewService.createReview(locationId, request, email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews(
            @PathVariable Long locationId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        logger.debug("Fetching reviews for location id={}", locationId);
        return ResponseEntity.ok(reviewService.getReviewsForLocation(locationId, sortBy, direction));
    }
}
