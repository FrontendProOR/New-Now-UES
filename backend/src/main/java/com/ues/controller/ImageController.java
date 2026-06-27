package com.ues.controller;

import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LogManager.getLogger(ImageController.class);

    private final MinioUtil minioUtil;

    public ImageController(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    /**
     * Serves image files from MinIO storage.
     * URL pattern: /api/images/{folder}/{filename}
     * Example: /api/images/locations/abc-123.jpg
     *
     * This endpoint is public (no auth required) so that <img> tags work without tokens.
     */
    @GetMapping("/**")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
        // Extract the object name from the URL path after "/api/images/"
        String fullPath = request.getRequestURI();
        String objectName = fullPath.substring(fullPath.indexOf("/api/images/") + "/api/images/".length());

        if (objectName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try (InputStream inputStream = minioUtil.getFile(objectName)) {
            byte[] imageBytes = inputStream.readAllBytes();

            MediaType mediaType = determineMediaType(objectName);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(imageBytes.length)
                    .header("Cache-Control", "public, max-age=86400")
                    .body(imageBytes);
        } catch (Exception e) {
            logger.error("Failed to serve image '{}': {}", objectName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private MediaType determineMediaType(String objectName) {
        String lower = objectName.toLowerCase();
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lower.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        } else if (lower.endsWith(".svg")) {
            return MediaType.parseMediaType("image/svg+xml");
        } else {
            // Default to JPEG for .jpg, .jpeg, and anything else
            return MediaType.IMAGE_JPEG;
        }
    }
}
