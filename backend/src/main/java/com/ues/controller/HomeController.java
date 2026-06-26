package com.ues.controller;

import com.ues.dto.EventDto;
import com.ues.dto.LocationDto;
import com.ues.dto.ReviewDto;
import com.ues.service.EventService;
import com.ues.service.LocationService;
import com.ues.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    private final LocationService locationService;
    private final EventService eventService;
    private final ReviewService reviewService;

    public HomeController(LocationService locationService,
                          EventService eventService,
                          ReviewService reviewService) {
        this.locationService = locationService;
        this.eventService = eventService;
        this.reviewService = reviewService;
    }

    @GetMapping("/popular-locations")
    public ResponseEntity<List<LocationDto>> getPopularLocations() {
        logger.debug("Fetching top 5 popular locations");
        return ResponseEntity.ok(locationService.getTopLocations(5));
    }

    @GetMapping("/latest-reviews")
    public ResponseEntity<List<ReviewDto>> getLatestReviews() {
        logger.debug("Fetching latest 3 reviews from most popular location");
        List<LocationDto> topLocations = locationService.getTopLocations(1);
        if (topLocations.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        Long topLocationId = topLocations.get(0).getId();
        return ResponseEntity.ok(reviewService.getLatestReviewsForLocation(topLocationId, 3));
    }

    @GetMapping("/today-events")
    public ResponseEntity<List<EventDto>> getTodayEvents() {
        logger.debug("Fetching today's events for home page");
        return ResponseEntity.ok(eventService.getTodayEvents());
    }
}
