package com.ues.dto;

import java.util.List;

public class AnalyticsDto {

    private long totalEvents;
    private long recurrentEvents;
    private long nonRecurrentEvents;
    private long freeEvents;
    private long paidEvents;

    private List<EventRatingDto> topEventsByRating;
    private List<EventRatingDto> bottomEventsByRating;
    private List<ReviewDto> latestReviewsFromTopLocation;

    public AnalyticsDto() {
    }

    public AnalyticsDto(long totalEvents, long recurrentEvents, long nonRecurrentEvents,
                        long freeEvents, long paidEvents, List<EventRatingDto> topEventsByRating,
                        List<EventRatingDto> bottomEventsByRating, List<ReviewDto> latestReviewsFromTopLocation) {
        this.totalEvents = totalEvents;
        this.recurrentEvents = recurrentEvents;
        this.nonRecurrentEvents = nonRecurrentEvents;
        this.freeEvents = freeEvents;
        this.paidEvents = paidEvents;
        this.topEventsByRating = topEventsByRating;
        this.bottomEventsByRating = bottomEventsByRating;
        this.latestReviewsFromTopLocation = latestReviewsFromTopLocation;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getRecurrentEvents() {
        return recurrentEvents;
    }

    public void setRecurrentEvents(long recurrentEvents) {
        this.recurrentEvents = recurrentEvents;
    }

    public long getNonRecurrentEvents() {
        return nonRecurrentEvents;
    }

    public void setNonRecurrentEvents(long nonRecurrentEvents) {
        this.nonRecurrentEvents = nonRecurrentEvents;
    }

    public long getFreeEvents() {
        return freeEvents;
    }

    public void setFreeEvents(long freeEvents) {
        this.freeEvents = freeEvents;
    }

    public long getPaidEvents() {
        return paidEvents;
    }

    public void setPaidEvents(long paidEvents) {
        this.paidEvents = paidEvents;
    }

    public List<EventRatingDto> getTopEventsByRating() {
        return topEventsByRating;
    }

    public void setTopEventsByRating(List<EventRatingDto> topEventsByRating) {
        this.topEventsByRating = topEventsByRating;
    }

    public List<EventRatingDto> getBottomEventsByRating() {
        return bottomEventsByRating;
    }

    public void setBottomEventsByRating(List<EventRatingDto> bottomEventsByRating) {
        this.bottomEventsByRating = bottomEventsByRating;
    }

    public List<ReviewDto> getLatestReviewsFromTopLocation() {
        return latestReviewsFromTopLocation;
    }

    public void setLatestReviewsFromTopLocation(List<ReviewDto> latestReviewsFromTopLocation) {
        this.latestReviewsFromTopLocation = latestReviewsFromTopLocation;
    }

    public static class EventRatingDto {

        private Long eventId;
        private String eventName;
        private String locationName;
        private Double averageRating;

        public EventRatingDto() {
        }

        public EventRatingDto(Long eventId, String eventName, String locationName, Double averageRating) {
            this.eventId = eventId;
            this.eventName = eventName;
            this.locationName = locationName;
            this.averageRating = averageRating;
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

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }
    }
}
