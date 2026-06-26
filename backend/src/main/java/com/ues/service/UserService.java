package com.ues.service;

import com.ues.dto.ChangePasswordRequest;
import com.ues.dto.LocationDto;
import com.ues.dto.ReviewDto;
import com.ues.dto.UserProfileDto;
import com.ues.model.Image;
import com.ues.model.Manages;
import com.ues.model.User;
import com.ues.repository.ImageRepository;
import com.ues.repository.ManagesRepository;
import com.ues.repository.UserRepository;
import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ManagesRepository managesRepository;
    private final ReviewService reviewService;
    private final LocationService locationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MinioUtil minioUtil;

    public UserService(UserRepository userRepository,
                       ImageRepository imageRepository,
                       ManagesRepository managesRepository,
                       ReviewService reviewService,
                       LocationService locationService,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder,
                       MinioUtil minioUtil) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.managesRepository = managesRepository;
        this.reviewService = reviewService;
        this.locationService = locationService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.minioUtil = minioUtil;
    }

    public UserProfileDto getProfile(String email) {
        User user = findByEmail(email);
        return toDto(user);
    }

    @Transactional
    public UserProfileDto updateProfile(String email, String name, String phoneNumber,
                                         LocalDate birthday, String address, String city,
                                         MultipartFile imageFile) throws Exception {
        User user = findByEmail(email);

        if (name != null) user.setName(name);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (birthday != null) user.setBirthday(birthday);
        if (address != null) user.setAddress(address);
        if (city != null) user.setCity(city);

        if (imageFile != null && !imageFile.isEmpty()) {
            if (user.getProfileImage() != null) {
                try {
                    minioUtil.deleteFile(user.getProfileImage().getServerFilename());
                } catch (Exception e) {
                    logger.warn("Failed to delete old profile image: {}", e.getMessage());
                }
                String newObjectName = minioUtil.uploadFile("profiles", imageFile);
                user.getProfileImage().setServerFilename(newObjectName);
            } else {
                String objectName = minioUtil.uploadFile("profiles", imageFile);
                Image image = new Image();
                image.setServerFilename(objectName);
                imageRepository.save(image);
                user.setProfileImage(image);
            }
        }

        User saved = userRepository.save(user);
        logger.info("Updated profile for user: {}", email);
        return toDto(saved);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findByEmail(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed for user: {}", email);
        emailService.sendPasswordChanged(email);
    }

    public List<ReviewDto> getUserReviews(String email) {
        User user = findByEmail(email);
        return reviewService.getReviewsByUser(user.getId());
    }

    public List<LocationDto> getManagedLocations(String email) {
        User user = findByEmail(email);
        List<Manages> assignments = managesRepository.findByUserId(user.getId());
        return assignments.stream()
                .filter(m -> m.getEndDate() == null || !m.getEndDate().isBefore(LocalDate.now()))
                .map(m -> locationService.getLocation(m.getLocation().getId()))
                .toList();
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserProfileDto toDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBirthday(user.getBirthday());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getProfileImage() != null) {
            dto.setImageUrl(minioUtil.getFileUrl(user.getProfileImage().getServerFilename()));
        }

        return dto;
    }
}
