package com.ues.controller;

import com.ues.dto.SearchResultDto;
import com.ues.model.Location;
import com.ues.repository.LocationRepository;
import com.ues.service.SearchService;
import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private static final Logger logger = LogManager.getLogger(SearchController.class);

    private final SearchService searchService;
    private final LocationRepository locationRepository;
    private final MinioUtil minioUtil;

    public SearchController(SearchService searchService,
                            LocationRepository locationRepository,
                            MinioUtil minioUtil) {
        this.searchService = searchService;
        this.locationRepository = locationRepository;
        this.minioUtil = minioUtil;
    }

    @GetMapping("/locations")
    public ResponseEntity<List<SearchResultDto>> searchLocations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String fileDescription,
            @RequestParam(required = false) Integer reviewCountFrom,
            @RequestParam(required = false) Integer reviewCountTo,
            @RequestParam(required = false) Float performanceFrom,
            @RequestParam(required = false) Float performanceTo,
            @RequestParam(required = false) Float soundFrom,
            @RequestParam(required = false) Float soundTo,
            @RequestParam(required = false) Float lightingFrom,
            @RequestParam(required = false) Float lightingTo,
            @RequestParam(required = false) Float spaceFrom,
            @RequestParam(required = false) Float spaceTo,
            @RequestParam(required = false) Float experienceFrom,
            @RequestParam(required = false) Float experienceTo,
            @RequestParam(defaultValue = "OR") String operator,
            @RequestParam(required = false) String mlt,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "name") String sortBy) {

        logger.info("Search request: name={}, desc={}, pdf={}, operator={}, sortBy={}", name, description, fileDescription, operator, sortBy);

        List<SearchResultDto> results = searchService.search(
                name, description, fileDescription,
                reviewCountFrom, reviewCountTo,
                performanceFrom, performanceTo,
                soundFrom, soundTo,
                lightingFrom, lightingTo,
                spaceFrom, spaceTo,
                experienceFrom, experienceTo,
                operator, mlt, sortDirection, sortBy);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/locations/{id}/pdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable Long id) throws Exception {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        if (location.getDescriptionDocument() == null) {
            throw new IllegalArgumentException("No PDF document found for this location");
        }

        String objectName = location.getDescriptionDocument().getServerFilename();
        InputStream stream = minioUtil.getFile(objectName);

        logger.info("Downloading PDF for location id={}", id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"location-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(stream));
    }
}
