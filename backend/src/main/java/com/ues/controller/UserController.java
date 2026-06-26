package com.ues.controller;

import com.ues.dto.*;
import com.ues.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching profile for user: {}", email);
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            Authentication authentication,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestPart(required = false) MultipartFile image) throws Exception {
        String email = authentication.getName();
        logger.info("Updating profile for user: {}", email);
        UserProfileDto dto = userService.updateProfile(email, name, phoneNumber, birthday, address, city, image);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                  @Valid @RequestBody ChangePasswordRequest request) {
        String email = authentication.getName();
        logger.info("Password change request for user: {}", email);
        userService.changePassword(email, request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> getUserReviews(Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching reviews for user: {}", email);
        return ResponseEntity.ok(userService.getUserReviews(email));
    }

    @GetMapping("/managed-locations")
    public ResponseEntity<List<LocationDto>> getManagedLocations(Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching managed locations for user: {}", email);
        return ResponseEntity.ok(userService.getManagedLocations(email));
    }
}
