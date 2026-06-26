package com.ues.service;

import com.ues.model.Location;
import com.ues.model.LocationIndex;
import com.ues.model.Rate;
import com.ues.model.Review;
import com.ues.repository.LocationRepository;
import com.ues.repository.LocationSearchRepository;
import com.ues.repository.ReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationIndexService {

    private static final Logger logger = LogManager.getLogger(LocationIndexService.class);

    private final LocationSearchRepository locationSearchRepository;
    private final LocationRepository locationRepository;
    private final ReviewRepository reviewRepository;

    public LocationIndexService(LocationSearchRepository locationSearchRepository,
                                 LocationRepository locationRepository,
                                 ReviewRepository reviewRepository) {
        this.locationSearchRepository = locationSearchRepository;
        this.locationRepository = locationRepository;
        this.reviewRepository = reviewRepository;
    }

    public void indexLocation(Location location) {
        LocationIndex index = buildIndex(location);
        locationSearchRepository.save(index);
        logger.info("Indexed location id={} in Elasticsearch", location.getId());
    }

    public void updateIndex(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        indexLocation(location);
    }

    public void deleteIndex(Long locationId) {
        locationSearchRepository.deleteById(locationId);
        logger.info("Deleted location id={} from Elasticsearch index", locationId);
    }

    public void updateFileDescription(Long locationId, String fileDescription) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        LocationIndex index = buildIndex(location);
        index.setFileDescription(fileDescription);
        locationSearchRepository.save(index);
        logger.info("Updated fileDescription in ES for location id={}", locationId);
    }

    public void reindexAll() {
        List<Location> allLocations = locationRepository.findAll();
        for (Location location : allLocations) {
            try {
                indexLocation(location);
            } catch (Exception e) {
                logger.error("Failed to index location id={}: {}", location.getId(), e.getMessage());
            }
        }
        logger.info("Reindexed all {} locations", allLocations.size());
    }

    private LocationIndex buildIndex(Location location) {
        List<Review> validReviews = reviewRepository
                .findByLocationIdAndDeletedFalse(location.getId());

        LocationIndex index = new LocationIndex();
        index.setId(location.getId());
        index.setName(location.getName());
        index.setDescription(location.getDescription());
        index.setReviewCount(validReviews.size());

        if (location.getDescriptionDocument() != null) {
            LocationIndex existing = locationSearchRepository.findById(location.getId()).orElse(null);
            if (existing != null && existing.getFileDescription() != null) {
                index.setFileDescription(existing.getFileDescription());
            }
        }

        if (!validReviews.isEmpty()) {
            index.setAvgPerformanceGrade(calculateAvg(validReviews, "performance"));
            index.setAvgSoundGrade(calculateAvg(validReviews, "soundAndLighting"));
            index.setAvgLightingGrade(calculateAvg(validReviews, "soundAndLighting"));
            index.setAvgSpaceGrade(calculateAvg(validReviews, "venue"));
            index.setAvgExperienceGrade(calculateAvg(validReviews, "overallImpression"));
        }

        return index;
    }

    private Float calculateAvg(List<Review> reviews, String field) {
        double sum = 0;
        int count = 0;

        for (Review r : reviews) {
            Rate rate = r.getRate();
            if (rate == null) continue;

            Integer value = switch (field) {
                case "performance" -> rate.getPerformance();
                case "soundAndLighting" -> rate.getSoundAndLighting();
                case "venue" -> rate.getVenue();
                case "overallImpression" -> rate.getOverallImpression();
                default -> null;
            };

            if (value != null) {
                sum += value;
                count++;
            }
        }

        return count > 0 ? (float) (sum / count) : null;
    }
}
