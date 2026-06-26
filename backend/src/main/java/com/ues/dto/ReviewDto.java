package com.ues.dto;

import java.time.LocalDateTime;

public class ReviewDto {

    private Long id;
    private LocalDateTime createdAt;
    private Integer eventCount;
    private Boolean hidden;

    private Long authorId;
    private String authorName;

    private Long locationId;
    private String locationName;

    private Long eventId;
    private String eventName;

    private RateDto rate;
    private String rootComment;

    public ReviewDto() {
    }

    public ReviewDto(Long id, LocalDateTime createdAt, Integer eventCount, Boolean hidden,
                     Long authorId, String authorName, Long locationId, String locationName,
                     Long eventId, String eventName, RateDto rate, String rootComment) {
        this.id = id;
        this.createdAt = createdAt;
        this.eventCount = eventCount;
        this.hidden = hidden;
        this.authorId = authorId;
        this.authorName = authorName;
        this.locationId = locationId;
        this.locationName = locationName;
        this.eventId = eventId;
        this.eventName = eventName;
        this.rate = rate;
        this.rootComment = rootComment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public RateDto getRate() {
        return rate;
    }

    public void setRate(RateDto rate) {
        this.rate = rate;
    }

    public String getRootComment() {
        return rootComment;
    }

    public void setRootComment(String rootComment) {
        this.rootComment = rootComment;
    }
}
