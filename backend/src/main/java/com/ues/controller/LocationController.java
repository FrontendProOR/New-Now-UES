package com.ues.controller;

import com.ues.dto.LocationDto;
import com.ues.service.LocationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private static final Logger logger = LogManager.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String type) {
        if (name != null || address != null || type != null) {
            logger.debug("Searching locations: name={}, address={}, type={}", name, address, type);
            return ResponseEntity.ok(locationService.searchLocations(name, address, type));
        }
        logger.debug("Fetching all locations");
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long id) {
        logger.debug("Fetching location id={}", id);
        return ResponseEntity.ok(locationService.getLocation(id));
    }
}
