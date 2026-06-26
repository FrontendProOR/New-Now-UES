package com.ues.service;

import com.ues.dto.CreateReviewRequest;
import com.ues.dto.RateDto;
import com.ues.dto.ReviewDto;
import com.ues.model.*;
import com.ues.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {

    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LocationIndexService locationIndexService;

    public ReviewService(ReviewRepository reviewRepository,
                         LocationRepository locationRepository,
                         EventRepository eventRepository,
                         UserRepository userRepository,
                         CommentRepository commentRepository,
                         @org.springframework.context.annotation.Lazy LocationIndexService locationIndexService) {
        this.reviewRepository = reviewRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.locationIndexService = locationIndexService;
    }

    @Transactional
    public ReviewDto createReview(Long locationId, CreateReviewRequest request, String userEmail) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getLocation().getId().equals(locationId)) {
            throw new IllegalArgumentException("Event does not belong to this location");
        }

        if (!event.getRecurrent()) {
            throw new IllegalStateException("Reviews can only be left for recurrent events");
        }

        if (event.getDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Event has not taken place yet");
        }

        // 5.7: eventCount = broj pojavljivanja događaja sa istim name i type na istom mestu do datuma recenzije
        int eventCount = eventRepository.countEventOccurrences(
                event.getName(), event.getType(), locationId, LocalDate.now());

        Rate rate = new Rate();
        rate.setPerformance(request.getRate().getPerformance());
        rate.setSoundAndLighting(request.getRate().getSoundAndLighting());
        rate.setVenue(request.getRate().getVenue());
        rate.setOverallImpression(request.getRate().getOverallImpression());

        Review review = new Review();
        review.setAuthor(author);
        review.setLocation(location);
        review.setEvent(event);
        review.setRate(rate);
        review.setEventCount(eventCount);

        Review saved = reviewRepository.save(review);

        // Opcioni koreni komentar
        if (request.getComment() != null && !request.getComment().isBlank()) {
            Comment comment = new Comment();
            comment.setText(request.getComment());
            comment.setAuthor(author);
            comment.setReview(saved);
            commentRepository.save(comment);
        }

        // Ažuriraj totalRating na lokaciji
        updateLocationRating(locationId);

        logger.info("Created review id={} for location id={} by user {}", saved.getId(), locationId, userEmail);
        return toDto(saved);
    }

    public List<ReviewDto> getReviewsForLocation(Long locationId, String sortBy, String direction) {
        Sort sort = buildSort(sortBy, direction);
        return reviewRepository.findByLocationIdAndDeletedFalseAndHiddenFalse(locationId, sort).stream()
                .map(this::toDto)
                .toList();
    }

    public List<ReviewDto> getAllReviewsForLocation(Long locationId, String sortBy, String direction) {
        Sort sort = buildSort(sortBy, direction);
        return reviewRepository.findByLocationIdAndDeletedFalse(locationId, sort).stream()
                .map(this::toDto)
                .toList();
    }

    public List<ReviewDto> getLatestReviewsForLocation(Long locationId, int count) {
        return reviewRepository.findTop3ByLocationIdAndDeletedFalseOrderByCreatedAtDesc(locationId).stream()
                .limit(count)
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ReviewDto hideReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setHidden(true);
        Review saved = reviewRepository.save(review);
        logger.info("Review id={} hidden", reviewId);
        return toDto(saved);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setDeleted(true);
        reviewRepository.save(review);

        updateLocationRating(review.getLocation().getId());
        logger.info("Review id={} logically deleted, totalRating recalculated for location id={}",
                reviewId, review.getLocation().getId());
    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        return reviewRepository.findByAuthorId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public void updateLocationRating(Long locationId) {
        Double avg = reviewRepository.calculateAverageRating(locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        location.setTotalRating(avg);
        locationRepository.save(location);

        try {
            locationIndexService.updateIndex(locationId);
        } catch (Exception e) {
            logger.warn("Failed to sync ES index after rating update for location id={}: {}", locationId, e.getMessage());
        }

        logger.debug("Updated totalRating for location id={}: {}", locationId, avg);
    }

    private Sort buildSort(String sortBy, String direction) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return switch (sortBy) {
            case "date" -> Sort.by(dir, "createdAt");
            case "rating" -> Sort.by(dir, "rate.overallImpression");
            default -> Sort.by(dir, "createdAt");
        };
    }

    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setEventCount(review.getEventCount());
        dto.setHidden(review.getHidden());

        dto.setAuthorId(review.getAuthor().getId());
        dto.setAuthorName(review.getAuthor().getName());

        dto.setLocationId(review.getLocation().getId());
        dto.setLocationName(review.getLocation().getName());

        dto.setEventId(review.getEvent().getId());
        dto.setEventName(review.getEvent().getName());

        if (review.getRate() != null) {
            RateDto rateDto = new RateDto();
            rateDto.setPerformance(review.getRate().getPerformance());
            rateDto.setSoundAndLighting(review.getRate().getSoundAndLighting());
            rateDto.setVenue(review.getRate().getVenue());
            rateDto.setOverallImpression(review.getRate().getOverallImpression());
            dto.setRate(rateDto);
        }

        if (review.getComments() != null && !review.getComments().isEmpty()) {
            review.getComments().stream()
                    .filter(c -> c.getParent() == null)
                    .findFirst()
                    .ifPresent(c -> dto.setRootComment(c.getText()));
        }

        return dto;
    }
}
