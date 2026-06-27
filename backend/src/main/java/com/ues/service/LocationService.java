package com.ues.service;

import com.ues.dto.LocationDto;
import com.ues.model.DescriptionDocument;
import com.ues.model.Image;
import com.ues.model.Location;
import com.ues.repository.*;
import com.ues.util.MinioUtil;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LocationService {

    private static final Logger logger = LogManager.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final ManagesRepository managesRepository;
    private final DescriptionDocumentRepository descriptionDocumentRepository;
    private final MinioUtil minioUtil;
    private final LocationIndexService locationIndexService;
    private final EntityManager entityManager;

    public LocationService(LocationRepository locationRepository,
                           ImageRepository imageRepository,
                           EventRepository eventRepository,
                           ReviewRepository reviewRepository,
                           CommentRepository commentRepository,
                           ManagesRepository managesRepository,
                           DescriptionDocumentRepository descriptionDocumentRepository,
                           MinioUtil minioUtil,
                           @Lazy LocationIndexService locationIndexService,
                           EntityManager entityManager) {
        this.locationRepository = locationRepository;
        this.imageRepository = imageRepository;
        this.eventRepository = eventRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
        this.managesRepository = managesRepository;
        this.descriptionDocumentRepository = descriptionDocumentRepository;
        this.locationIndexService = locationIndexService;
        this.minioUtil = minioUtil;
        this.entityManager = entityManager;
    }

    @Transactional
    public LocationDto createLocation(String name, String description, String address,
                                       String type, MultipartFile imageFile) throws Exception {
        String objectName = minioUtil.uploadFile("locations", imageFile);

        Image image = new Image();
        image.setServerFilename(objectName);
        imageRepository.save(image);

        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setAddress(address);
        location.setType(type);
        location.setImage(image);

        Location saved = locationRepository.save(location);
        logger.info("Created location: {} (id={})", name, saved.getId());

        try {
            locationIndexService.indexLocation(saved);
        } catch (Exception e) {
            logger.warn("Failed to index new location in ES: {}", e.getMessage());
        }

        return toDto(saved);
    }

    @Transactional
    public LocationDto updateLocation(Long id, String name, String description,
                                       String address, String type,
                                       MultipartFile imageFile) throws Exception {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));

        location.setName(name);
        location.setDescription(description);
        location.setAddress(address);
        location.setType(type);

        if (imageFile != null && !imageFile.isEmpty()) {
            String oldFilename = null;
            if (location.getImage() != null) {
                oldFilename = location.getImage().getServerFilename();
            }

            String newObjectName = minioUtil.uploadFile("locations", imageFile);

            if (oldFilename != null) {
                try {
                    minioUtil.deleteFile(oldFilename);
                } catch (Exception e) {
                    logger.warn("Failed to delete old image {}: {}", oldFilename, e.getMessage());
                }
            }

            if (location.getImage() != null) {
                location.getImage().setServerFilename(newObjectName);
                imageRepository.save(location.getImage());
            } else {
                Image image = new Image();
                image.setServerFilename(newObjectName);
                imageRepository.save(image);
                location.setImage(image);
            }
        }

        Location saved = locationRepository.save(location);
        logger.info("Updated location id={}", id);

        try {
            locationIndexService.updateIndex(id);
        } catch (Exception e) {
            logger.warn("Failed to update ES index for location id={}: {}", id, e.getMessage());
        }

        return toDto(saved);
    }

    @Transactional
    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));

        // 1. Delete all comments for all reviews of this location
        //    Must delete replies (children) before parents due to self-referencing FK
        var allReviews = reviewRepository.findAll().stream()
                .filter(r -> r.getLocation().getId().equals(id))
                .toList();
        for (var review : allReviews) {
            // First delete replies (comments that have a parent)
            var replies = commentRepository.findByReviewIdAndParentIsNotNull(review.getId());
            commentRepository.deleteAll(replies);
            // Then delete top-level comments
            var topLevelComments = commentRepository.findByReviewIdAndParentIsNull(review.getId());
            commentRepository.deleteAll(topLevelComments);
        }
        entityManager.flush();

        // 2. Delete all reviews (cascade=ALL on Review.rate will delete Rate entities)
        reviewRepository.deleteAll(allReviews);
        entityManager.flush();
        logger.info("Deleted {} reviews for location id={}", allReviews.size(), id);

        // 3. Delete events: break FK to Image first, then delete Image, then delete Event
        var events = eventRepository.findByLocationId(id);
        var eventImageIds = new java.util.ArrayList<Long>();
        for (var event : events) {
            if (event.getImage() != null) {
                eventImageIds.add(event.getImage().getId());
                try {
                    minioUtil.deleteFile(event.getImage().getServerFilename());
                } catch (Exception e) {
                    logger.warn("Failed to delete event image from MinIO: {}", e.getMessage());
                }
                // Break the FK reference before deleting the Image
                event.setImage(null);
                eventRepository.save(event);
            }
        }
        entityManager.flush();
        // Now delete the orphaned Image entities
        for (Long imageId : eventImageIds) {
            imageRepository.deleteById(imageId);
        }
        // Delete the events themselves
        eventRepository.deleteAll(events);
        entityManager.flush();
        logger.info("Deleted {} events for location id={}", events.size(), id);

        // 4. Delete manages records
        managesRepository.deleteAll(managesRepository.findByLocationId(id));
        entityManager.flush();

        // 5. Break FK references from Location to Image and DescriptionDocument
        Image locationImage = location.getImage();
        DescriptionDocument locationDoc = location.getDescriptionDocument();

        // Delete files from MinIO
        if (locationImage != null) {
            try {
                minioUtil.deleteFile(locationImage.getServerFilename());
            } catch (Exception e) {
                logger.warn("Failed to delete location image from MinIO: {}", e.getMessage());
            }
        }
        if (locationDoc != null) {
            try {
                minioUtil.deleteFile(locationDoc.getServerFilename());
            } catch (Exception e) {
                logger.warn("Failed to delete PDF from MinIO: {}", e.getMessage());
            }
        }

        // Null out FK references and save, so Hibernate no longer tracks them from Location
        location.setImage(null);
        location.setDescriptionDocument(null);
        locationRepository.save(location);
        entityManager.flush();

        // 6. Delete the orphaned Image and DescriptionDocument entities
        if (locationImage != null) {
            imageRepository.deleteById(locationImage.getId());
        }
        if (locationDoc != null) {
            descriptionDocumentRepository.deleteById(locationDoc.getId());
        }
        entityManager.flush();

        // 7. Delete the location itself
        locationRepository.delete(location);

        // 8. Delete from ES index
        try {
            locationIndexService.deleteIndex(id);
        } catch (Exception e) {
            logger.warn("Failed to delete ES index for location id={}: {}", id, e.getMessage());
        }

        logger.info("Deleted location id={} with all dependencies", id);
    }

    public LocationDto getLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
        return toDto(location);
    }

    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<LocationDto> searchLocations(String name, String address, String type) {
        return locationRepository.searchLocations(name, address, type).stream()
                .map(this::toDto)
                .toList();
    }

    public List<LocationDto> getTopLocations(int count) {
        return locationRepository.findTop5ByOrderByTotalRatingDesc().stream()
                .limit(count)
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public LocationDto updateLocationPartial(Long id, String address, String type, String description) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));

        if (address != null) location.setAddress(address);
        if (type != null) location.setType(type);
        if (description != null) location.setDescription(description);

        Location saved = locationRepository.save(location);
        logger.info("Partially updated location id={}", id);
        return toDto(saved);
    }

    private LocationDto toDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setDescription(location.getDescription());
        dto.setAddress(location.getAddress());
        dto.setType(location.getType());
        dto.setTotalRating(location.getTotalRating());
        dto.setCreatedAt(location.getCreatedAt());

        if (location.getImage() != null) {
            dto.setImageUrl("/api/images/" + location.getImage().getServerFilename());
        }

        return dto;
    }
}
