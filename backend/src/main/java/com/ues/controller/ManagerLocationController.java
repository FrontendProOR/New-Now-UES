package com.ues.controller;

import com.ues.dto.LocationDto;
import com.ues.service.LocationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manager/locations")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
public class ManagerLocationController {

    private static final Logger logger = LogManager.getLogger(ManagerLocationController.class);

    private final LocationService locationService;

    public ManagerLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long id,
                                                       @RequestBody Map<String, String> body) {
        String address = body.get("address");
        String type = body.get("type");
        String description = body.get("description");

        logger.info("Manager updating location id={}", id);
        LocationDto dto = locationService.updateLocationPartial(id, address, type, description);
        return ResponseEntity.ok(dto);
    }
}
