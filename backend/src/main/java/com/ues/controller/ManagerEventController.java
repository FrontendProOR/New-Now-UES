package com.ues.controller;

import com.ues.dto.EventDto;
import com.ues.service.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
public class ManagerEventController {

    private static final Logger logger = LogManager.getLogger(ManagerEventController.class);

    private final EventService eventService;

    public ManagerEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/locations/{locationId}/events")
    public ResponseEntity<java.util.List<EventDto>> getEvents(@PathVariable Long locationId) {
        logger.info("Manager fetching events for location id={}", locationId);
        return ResponseEntity.ok(eventService.getEventsByLocation(locationId));
    }

    @PostMapping("/locations/{locationId}/events")
    public ResponseEntity<EventDto> createEvent(
            @PathVariable Long locationId,
            @RequestParam String name,
            @RequestParam(required = false) String address,
            @RequestParam String type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Boolean recurrent,
            @RequestPart MultipartFile image) throws Exception {
        logger.info("Manager creating event: {} at location id={}", name, locationId);
        EventDto dto = eventService.createEvent(locationId, name, address, type, date, price, recurrent, image);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Boolean recurrent,
            @RequestPart(required = false) MultipartFile image) throws Exception {
        logger.info("Manager updating event id={}", id);
        EventDto dto = eventService.updateEvent(id, name, address, type, date, price, recurrent, image);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Manager deleting event id={}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
