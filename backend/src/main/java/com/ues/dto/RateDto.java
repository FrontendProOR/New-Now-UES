package com.ues.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RateDto {

    @Min(1) @Max(10)
    private Integer performance;

    @Min(1) @Max(10)
    private Integer soundAndLighting;

    @Min(1) @Max(10)
    private Integer venue;

    @Min(1) @Max(10)
    private Integer overallImpression;

    public RateDto() {
    }

    public RateDto(Integer performance, Integer soundAndLighting, Integer venue, Integer overallImpression) {
        this.performance = performance;
        this.soundAndLighting = soundAndLighting;
        this.venue = venue;
        this.overallImpression = overallImpression;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public Integer getSoundAndLighting() {
        return soundAndLighting;
    }

    public void setSoundAndLighting(Integer soundAndLighting) {
        this.soundAndLighting = soundAndLighting;
    }

    public Integer getVenue() {
        return venue;
    }

    public void setVenue(Integer venue) {
        this.venue = venue;
    }

    public Integer getOverallImpression() {
        return overallImpression;
    }

    public void setOverallImpression(Integer overallImpression) {
        this.overallImpression = overallImpression;
    }
}
