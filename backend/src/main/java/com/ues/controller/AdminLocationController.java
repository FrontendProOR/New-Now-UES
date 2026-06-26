package com.ues.controller;

import com.ues.dto.LocationDto;
import com.ues.service.LocationService;
import com.ues.service.PdfService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/locations")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminLocationController {

    private static final Logger logger = LogManager.getLogger(AdminLocationController.class);

    private final LocationService locationService;
    private final PdfService pdfService;

    public AdminLocationController(LocationService locationService, PdfService pdfService) {
        this.locationService = locationService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String type,
            @RequestParam(required = false) String description,
            @RequestPart MultipartFile image) throws Exception {
        logger.info("Admin creating location: {}", name);
        LocationDto dto = locationService.createLocation(name, description, address, type, image);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String type,
            @RequestParam(required = false) String description,
            @RequestPart(required = false) MultipartFile image) throws Exception {
        logger.info("Admin updating location id={}", id);
        LocationDto dto = locationService.updateLocation(id, name, description, address, type, image);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        logger.info("Admin deleting location id={}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pdf")
    public ResponseEntity<String> uploadPdf(@PathVariable Long id,
                                             @RequestPart MultipartFile pdf) throws Exception {
        logger.info("Admin uploading PDF for location id={}", id);
        pdfService.uploadPdf(id, pdf);
        return ResponseEntity.ok("PDF uploaded and indexed successfully");
    }
}
