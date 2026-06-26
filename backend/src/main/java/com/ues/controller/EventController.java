package com.ues.controller;

import com.ues.dto.EventDto;
import com.ues.service.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LogManager.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        if (type != null || locationId != null || address != null ||
                minPrice != null || maxPrice != null || dateFrom != null || dateTo != null) {
            logger.debug("Searching events with filters");
            return ResponseEntity.ok(eventService.searchEvents(type, locationId, address,
                    minPrice, maxPrice, dateFrom, dateTo));
        }
        logger.debug("Fetching all events");
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/today")
    public ResponseEntity<List<EventDto>> getTodayEvents() {
        logger.debug("Fetching today's events");
        return ResponseEntity.ok(eventService.getTodayEvents());
    }
}
