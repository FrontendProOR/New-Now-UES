package com.ues.service;

import com.ues.dto.LocationDto;
import com.ues.model.Image;
import com.ues.model.Location;
import com.ues.repository.ImageRepository;
import com.ues.repository.LocationRepository;
import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LocationService {

    private static final Logger logger = LogManager.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final MinioUtil minioUtil;

    public LocationService(LocationRepository locationRepository,
                           ImageRepository imageRepository,
                           MinioUtil minioUtil) {
        this.locationRepository = locationRepository;
        this.imageRepository = imageRepository;
        this.minioUtil = minioUtil;
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
        return toDto(saved);
    }

    @Transactional
    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));

        // MinIO brisanje PRE baze
        if (location.getImage() != null) {
            try {
                minioUtil.deleteFile(location.getImage().getServerFilename());
                logger.info("Deleted image from MinIO for location id={}", id);
            } catch (Exception e) {
                logger.error("Failed to delete image from MinIO for location id={}: {}", id, e.getMessage());
                throw new RuntimeException("Failed to delete image from storage. Location not deleted.", e);
            }
        }

        if (location.getDescriptionDocument() != null) {
            try {
                minioUtil.deleteFile(location.getDescriptionDocument().getServerFilename());
                logger.info("Deleted PDF from MinIO for location id={}", id);
            } catch (Exception e) {
                logger.warn("Failed to delete PDF from MinIO for location id={}: {}", id, e.getMessage());
            }
        }

        locationRepository.delete(location);
        logger.info("Deleted location id={}", id);
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
            dto.setImageUrl(minioUtil.getFileUrl(location.getImage().getServerFilename()));
        }

        return dto;
    }
}
