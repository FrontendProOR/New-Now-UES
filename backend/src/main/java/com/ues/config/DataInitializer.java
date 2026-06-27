package com.ues.config;

import com.ues.model.Administrator;
import com.ues.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@ues.com").isEmpty()) {
            Administrator admin = new Administrator();
            admin.setEmail("admin@ues.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin");
            admin.setAddress("Novi Sad");
            admin.setCity("Novi Sad");
            admin.setRole("ROLE_ADMIN");
            admin.setCreatedAt(LocalDate.now());
            userRepository.save(admin);
            logger.info("Default admin created: admin@ues.com / admin123");
        } else {
            logger.info("Admin already exists, skipping initialization");
        }
    }
}
