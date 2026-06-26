package com.ues;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UesApplication {

    private static final Logger logger = LogManager.getLogger(UesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UesApplication.class, args);
        logger.info("UES Application started successfully");
    }
}
