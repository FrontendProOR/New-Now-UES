package com.ues.service;

import com.ues.dto.ManagerDto;
import com.ues.model.Location;
import com.ues.model.Manages;
import com.ues.model.User;
import com.ues.repository.LocationRepository;
import com.ues.repository.ManagesRepository;
import com.ues.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ManagesService {

    private static final Logger logger = LogManager.getLogger(ManagesService.class);

    private final ManagesRepository managesRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public ManagesService(ManagesRepository managesRepository,
                          UserRepository userRepository,
                          LocationRepository locationRepository) {
        this.managesRepository = managesRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public Manages assignManager(Long locationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        if (managesRepository.existsByUserIdAndLocationIdAndEndDateIsNull(user.getId(), locationId)) {
            throw new IllegalStateException("User is already an active manager of this location");
        }

        user.setRole("ROLE_MANAGER");
        userRepository.save(user);

        Manages manages = new Manages();
        manages.setUser(user);
        manages.setLocation(location);
        manages.setStartDate(LocalDate.now());

        Manages saved = managesRepository.save(manages);
        logger.info("Assigned user email={} as manager of location id={}", email, locationId);
        return saved;
    }

    public List<ManagerDto> getManagersForLocation(Long locationId) {
        locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        return managesRepository.findByLocationId(locationId).stream()
                .filter(m -> m.getEndDate() == null)
                .map(m -> {
                    User user = m.getUser();
                    return new ManagerDto(user.getId(), user.getEmail(), user.getName(), m.getStartDate());
                })
                .toList();
    }

    @Transactional
    public void removeManager(Long locationId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));

        Manages manages = managesRepository.findByUserId(userId).stream()
                .filter(m -> m.getLocation().getId().equals(locationId) && m.getEndDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not an active manager of this location"));

        manages.setEndDate(LocalDate.now());
        managesRepository.save(manages);

        boolean hasOtherActiveAssignments = managesRepository.findByUserId(userId).stream()
                .anyMatch(m -> m.getEndDate() == null && !m.getLocation().getId().equals(locationId));

        if (!hasOtherActiveAssignments) {
            User user = manages.getUser();
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }

        logger.info("Removed user id={} as manager of location id={}", userId, locationId);
    }
}
