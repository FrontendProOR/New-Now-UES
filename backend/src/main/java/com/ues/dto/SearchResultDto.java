package com.ues.dto;

import java.util.Map;

public class SearchResultDto {

    private Long id;
    private String name;
    private String description;
    private Integer reviewCount;
    private Float avgPerformanceGrade;
    private Float avgSoundGrade;
    private Float avgLightingGrade;
    private Float avgSpaceGrade;
    private Float avgExperienceGrade;
    private Map<String, String> highlights;

    public SearchResultDto() {
    }

    public SearchResultDto(Long id, String name, String description, Integer reviewCount,
                           Float avgPerformanceGrade, Float avgSoundGrade, Float avgLightingGrade,
                           Float avgSpaceGrade, Float avgExperienceGrade, Map<String, String> highlights) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reviewCount = reviewCount;
        this.avgPerformanceGrade = avgPerformanceGrade;
        this.avgSoundGrade = avgSoundGrade;
        this.avgLightingGrade = avgLightingGrade;
        this.avgSpaceGrade = avgSpaceGrade;
        this.avgExperienceGrade = avgExperienceGrade;
        this.highlights = highlights;
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

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Float getAvgPerformanceGrade() {
        return avgPerformanceGrade;
    }

    public void setAvgPerformanceGrade(Float avgPerformanceGrade) {
        this.avgPerformanceGrade = avgPerformanceGrade;
    }

    public Float getAvgSoundGrade() {
        return avgSoundGrade;
    }

    public void setAvgSoundGrade(Float avgSoundGrade) {
        this.avgSoundGrade = avgSoundGrade;
    }

    public Float getAvgLightingGrade() {
        return avgLightingGrade;
    }

    public void setAvgLightingGrade(Float avgLightingGrade) {
        this.avgLightingGrade = avgLightingGrade;
    }

    public Float getAvgSpaceGrade() {
        return avgSpaceGrade;
    }

    public void setAvgSpaceGrade(Float avgSpaceGrade) {
        this.avgSpaceGrade = avgSpaceGrade;
    }

    public Float getAvgExperienceGrade() {
        return avgExperienceGrade;
    }

    public void setAvgExperienceGrade(Float avgExperienceGrade) {
        this.avgExperienceGrade = avgExperienceGrade;
    }

    public Map<String, String> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, String> highlights) {
        this.highlights = highlights;
    }
}
