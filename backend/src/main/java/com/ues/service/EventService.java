package com.ues.service;

import com.ues.dto.EventDto;
import com.ues.model.Event;
import com.ues.model.Image;
import com.ues.model.Location;
import com.ues.repository.EventRepository;
import com.ues.repository.ImageRepository;
import com.ues.repository.LocationRepository;
import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private static final Logger logger = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final MinioUtil minioUtil;

    public EventService(EventRepository eventRepository,
                        LocationRepository locationRepository,
                        ImageRepository imageRepository,
                        MinioUtil minioUtil) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.imageRepository = imageRepository;
        this.minioUtil = minioUtil;
    }

    @Transactional
    public EventDto createEvent(Long locationId, String name, String address, String type,
                                 LocalDate date, Double price, Boolean recurrent,
                                 MultipartFile imageFile) throws Exception {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        String objectName = minioUtil.uploadFile("events", imageFile);

        Image image = new Image();
        image.setServerFilename(objectName);
        imageRepository.save(image);

        Event event = new Event();
        event.setName(name);
        event.setAddress(address != null ? address : location.getAddress());
        event.setType(type);
        event.setDate(date);
        event.setPrice(price != null ? price : 0.0);
        event.setRecurrent(recurrent != null ? recurrent : false);
        event.setLocation(location);
        event.setImage(image);

        Event saved = eventRepository.save(event);
        logger.info("Created event: {} (id={}) at location id={}", name, saved.getId(), locationId);

        return toDto(saved);
    }

    @Transactional
    public EventDto updateEvent(Long id, String name, String address, String type,
                                 LocalDate date, Double price, Boolean recurrent,
                                 MultipartFile imageFile) throws Exception {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));

        if (name != null) event.setName(name);
        if (address != null) event.setAddress(address);
        if (type != null) event.setType(type);
        if (date != null) event.setDate(date);
        if (price != null) event.setPrice(price);
        if (recurrent != null) event.setRecurrent(recurrent);

        if (imageFile != null && !imageFile.isEmpty()) {
            String oldFilename = null;
            if (event.getImage() != null) {
                oldFilename = event.getImage().getServerFilename();
            }

            String newObjectName = minioUtil.uploadFile("events", imageFile);

            if (oldFilename != null) {
                try {
                    minioUtil.deleteFile(oldFilename);
                } catch (Exception e) {
                    logger.warn("Failed to delete old event image {}: {}", oldFilename, e.getMessage());
                }
            }

            if (event.getImage() != null) {
                event.getImage().setServerFilename(newObjectName);
                imageRepository.save(event.getImage());
            } else {
                Image image = new Image();
                image.setServerFilename(newObjectName);
                imageRepository.save(image);
                event.setImage(image);
            }
        }

        Event saved = eventRepository.save(event);
        logger.info("Updated event id={}", id);
        return toDto(saved);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));

        if (event.getImage() != null) {
            try {
                minioUtil.deleteFile(event.getImage().getServerFilename());
                logger.info("Deleted image from MinIO for event id={}", id);
            } catch (Exception e) {
                logger.error("Failed to delete image from MinIO for event id={}: {}", id, e.getMessage());
                throw new RuntimeException("Failed to delete image from storage. Event not deleted.", e);
            }
        }

        eventRepository.delete(event);
        logger.info("Deleted event id={}", id);
    }

    public EventDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
        return toDto(event);
    }

    public List<EventDto> getEventsByLocation(Long locationId) {
        return eventRepository.findByLocationId(locationId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<EventDto> getTodayEvents() {
        return eventRepository.findByDate(LocalDate.now()).stream()
                .map(this::toDto)
                .toList();
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private EventDto toDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setAddress(event.getAddress());
        dto.setType(event.getType());
        dto.setDate(event.getDate());
        dto.setPrice(event.getPrice());
        dto.setRecurrent(event.getRecurrent());
        dto.setLocationId(event.getLocation().getId());
        dto.setLocationName(event.getLocation().getName());

        if (event.getImage() != null) {
            dto.setImageUrl(minioUtil.getFileUrl(event.getImage().getServerFilename()));
        }

        return dto;
    }
}
