package com.ues.util;

import io.minio.*;
import io.minio.http.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class MinioUtil {

    private static final Logger logger = LogManager.getLogger(MinioUtil.class);

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                logger.info("Created MinIO bucket: {}", bucketName);
            } else {
                logger.info("MinIO bucket '{}' already exists", bucketName);
            }
        } catch (Exception e) {
            logger.warn("MinIO is not available at startup. Bucket '{}' will be created on first upload. Error: {}",
                    bucketName, e.getMessage());
        }
    }

    /**
     * Ensures the bucket exists before performing operations.
     * Called lazily when MinIO was not available at startup.
     */
    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                logger.info("Lazily created MinIO bucket: {}", bucketName);
            }
        } catch (Exception e) {
            logger.error("Failed to ensure MinIO bucket exists: {}", e.getMessage());
        }
    }

    public String uploadFile(String folder, MultipartFile file) throws Exception {
        ensureBucketExists();
        String extension = getExtension(file.getOriginalFilename());
        String objectName = folder + "/" + UUID.randomUUID() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        }

        logger.info("Uploaded file to MinIO: {}", objectName);
        return objectName;
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        logger.info("Deleted file from MinIO: {}", objectName);
    }

    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());
        } catch (Exception e) {
            logger.error("Failed to get URL for {}: {}", objectName, e.getMessage());
            return null;
        }
    }

    public InputStream getFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    private String getExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
}
