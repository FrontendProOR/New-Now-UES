package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private String city;
    private String role;
    private LocalDate createdAt;
    private String imageUrl;
}
