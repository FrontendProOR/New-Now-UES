package com.ues.dto;

import java.time.LocalDate;

public class EventDto {

    private Long id;
    private String name;
    private String address;
    private String type;
    private LocalDate date;
    private Double price;
    private Boolean recurrent;
    private Long locationId;
    private String locationName;
    private String imageUrl;

    public EventDto() {
    }

    public EventDto(Long id, String name, String address, String type, LocalDate date, Double price, Boolean recurrent, Long locationId, String locationName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.date = date;
        this.price = price;
        this.recurrent = recurrent;
        this.locationId = locationId;
        this.locationName = locationName;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getRecurrent() {
        return recurrent;
    }

    public void setRecurrent(Boolean recurrent) {
        this.recurrent = recurrent;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
