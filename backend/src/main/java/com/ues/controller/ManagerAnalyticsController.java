package com.ues.controller;

import com.ues.dto.AnalyticsDto;
import com.ues.service.AnalyticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/manager/locations/{locationId}/analytics")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
public class ManagerAnalyticsController {

    private static final Logger logger = LogManager.getLogger(ManagerAnalyticsController.class);

    private final AnalyticsService analyticsService;

    public ManagerAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsDto> getAnalytics(
            @PathVariable Long locationId,
            @RequestParam(defaultValue = "monthly") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        logger.info("Manager requesting analytics for location id={}, period={}", locationId, period);
        return ResponseEntity.ok(analyticsService.getAnalytics(locationId, period, dateFrom, dateTo));
    }
}
