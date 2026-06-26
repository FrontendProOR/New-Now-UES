package com.ues.service;

import com.ues.dto.AnalyticsDto;
import com.ues.dto.ReviewDto;
import com.ues.model.Event;
import com.ues.model.Review;
import com.ues.repository.EventRepository;
import com.ues.repository.ReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final Logger logger = LogManager.getLogger(AnalyticsService.class);

    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public AnalyticsService(EventRepository eventRepository,
                            ReviewRepository reviewRepository,
                            ReviewService reviewService) {
        this.eventRepository = eventRepository;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    public AnalyticsDto getAnalytics(Long locationId, String period,
                                      LocalDate customFrom, LocalDate customTo) {
        LocalDate now = LocalDate.now();
        LocalDate dateFrom;
        LocalDate dateTo = now;

        switch (period != null ? period.toLowerCase() : "monthly") {
            case "weekly" -> dateFrom = now.minusWeeks(1);
            case "yearly" -> dateFrom = now.minusYears(1);
            case "custom" -> {
                dateFrom = customFrom != null ? customFrom : now.minusMonths(1);
                dateTo = customTo != null ? customTo : now;
            }
            default -> dateFrom = now.minusMonths(1);
        }

        List<Event> events = eventRepository.findByLocationIdAndDateBetween(locationId, dateFrom, dateTo);

        AnalyticsDto dto = new AnalyticsDto();
        dto.setTotalEvents(events.size());
        dto.setRecurrentEvents(events.stream().filter(Event::getRecurrent).count());
        dto.setNonRecurrentEvents(events.stream().filter(e -> !e.getRecurrent()).count());
        dto.setFreeEvents(events.stream().filter(e -> e.getPrice() == null || e.getPrice() == 0.0).count());
        dto.setPaidEvents(events.stream().filter(e -> e.getPrice() != null && e.getPrice() > 0.0).count());

        dto.setTopEventsByRating(getEventsByAvgRating(events, true));
        dto.setBottomEventsByRating(getEventsByAvgRating(events, false));

        List<ReviewDto> latestReviews = reviewService.getLatestReviewsForLocation(locationId, 3);
        dto.setLatestReviewsFromTopLocation(latestReviews);

        logger.info("Generated analytics for location id={}, period={}", locationId, period);
        return dto;
    }

    private List<AnalyticsDto.EventRatingDto> getEventsByAvgRating(List<Event> events, boolean descending) {
        Map<Long, List<Review>> reviewsByEvent = events.stream()
                .collect(Collectors.toMap(
                        Event::getId,
                        e -> reviewRepository.findByLocationIdAndDeletedFalse(e.getLocation().getId()).stream()
                                .filter(r -> r.getEvent().getId().equals(e.getId()))
                                .toList(),
                        (a, b) -> a
                ));

        Comparator<AnalyticsDto.EventRatingDto> comparator = Comparator
                .comparingDouble(dto -> dto.getAverageRating() != null ? dto.getAverageRating() : 0.0);

        if (descending) {
            comparator = comparator.reversed();
        }

        return events.stream()
                .map(event -> {
                    List<Review> eventReviews = reviewsByEvent.getOrDefault(event.getId(), List.of());
                    Double avg = calculateAvgFromReviews(eventReviews);
                    return new AnalyticsDto.EventRatingDto(
                            event.getId(),
                            event.getName(),
                            event.getLocation().getName(),
                            avg
                    );
                })
                .filter(dto -> dto.getAverageRating() != null)
                .sorted(comparator)
                .limit(5)
                .toList();
    }

    private Double calculateAvgFromReviews(List<Review> reviews) {
        if (reviews.isEmpty()) return null;

        double sum = 0;
        int count = 0;
        for (Review r : reviews) {
            if (r.getRate() == null) continue;
            int localSum = 0;
            int localCount = 0;
            if (r.getRate().getPerformance() != null) { localSum += r.getRate().getPerformance(); localCount++; }
            if (r.getRate().getSoundAndLighting() != null) { localSum += r.getRate().getSoundAndLighting(); localCount++; }
            if (r.getRate().getVenue() != null) { localSum += r.getRate().getVenue(); localCount++; }
            if (r.getRate().getOverallImpression() != null) { localSum += r.getRate().getOverallImpression(); localCount++; }
            if (localCount > 0) {
                sum += (double) localSum / localCount;
                count++;
            }
        }
        return count > 0 ? sum / count : null;
    }
}
