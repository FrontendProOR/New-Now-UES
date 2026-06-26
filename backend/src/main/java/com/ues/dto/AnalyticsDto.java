package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {

    private long totalEvents;
    private long recurrentEvents;
    private long nonRecurrentEvents;
    private long freeEvents;
    private long paidEvents;

    private List<EventRatingDto> topEventsByRating;
    private List<EventRatingDto> bottomEventsByRating;
    private List<ReviewDto> latestReviewsFromTopLocation;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventRatingDto {
        private Long eventId;
        private String eventName;
        private String locationName;
        private Double averageRating;
    }
}
