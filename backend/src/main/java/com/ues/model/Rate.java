package com.ues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(10)
    private Integer performance;

    @Min(1)
    @Max(10)
    private Integer soundAndLighting;

    @Min(1)
    @Max(10)
    private Integer venue;

    @Min(1)
    @Max(10)
    private Integer overallImpression;

    @JsonIgnore
    @OneToOne(mappedBy = "rate")
    private Review review;

    public Rate() {
    }

    public Rate(Long id, Integer performance, Integer soundAndLighting, Integer venue, Integer overallImpression, Review review) {
        this.id = id;
        this.performance = performance;
        this.soundAndLighting = soundAndLighting;
        this.venue = venue;
        this.overallImpression = overallImpression;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
