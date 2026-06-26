package com.ues.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateReviewRequest {

    @NotNull
    private Long eventId;

    @NotNull
    @Valid
    private RateDto rate;

    private String comment;

    public CreateReviewRequest() {
    }

    public CreateReviewRequest(Long eventId, RateDto rate, String comment) {
        this.eventId = eventId;
        this.rate = rate;
        this.comment = comment;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public RateDto getRate() {
        return rate;
    }

    public void setRate(RateDto rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
