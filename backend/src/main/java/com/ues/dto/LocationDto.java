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
public class LocationDto {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String type;
    private Double totalRating;
    private LocalDate createdAt;
    private String imageUrl;
}
