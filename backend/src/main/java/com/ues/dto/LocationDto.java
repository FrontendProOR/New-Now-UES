package com.ues.dto;

import java.time.LocalDate;

public class LocationDto {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String type;
    private Double totalRating;
    private LocalDate createdAt;
    private String imageUrl;

    public LocationDto() {
    }

    public LocationDto(Long id, String name, String description, String address, String type, Double totalRating, LocalDate createdAt, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.type = type;
        this.totalRating = totalRating;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
