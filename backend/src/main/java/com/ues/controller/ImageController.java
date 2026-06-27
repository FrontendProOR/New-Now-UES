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
public class ImageController {

    private static final Logger logger = LogManager.getLogger(ImageController.class);
    private static final String PATH_PREFIX = "/api/images/";

    private final MinioUtil minioUtil;

    public ImageController(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    @GetMapping("/api/images/**")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        int prefixIndex = requestUri.indexOf(PATH_PREFIX);
        if (prefixIndex == -1) {
            logger.warn("Invalid image request: {}", requestUri);
            return ResponseEntity.badRequest().build();
        }

        String objectName = requestUri.substring(prefixIndex + PATH_PREFIX.length());
        if (objectName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        logger.debug("Serving image: {}", objectName);

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
        if (lower.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (lower.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (lower.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        if (lower.endsWith(".svg")) return MediaType.parseMediaType("image/svg+xml");
        return MediaType.IMAGE_JPEG;
    }
}
