package com.ues.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "locations")
@Setting(settingPath = "/elasticsearch/settings.json")
public class LocationIndex {

    @Id
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "serbian_custom_analyzer"),
            otherFields = {
                    @InnerField(suffix = "sort", type = FieldType.Keyword, normalizer = "lowercase_normalizer")
            }
    )
    private String name;

    @Field(type = FieldType.Text, analyzer = "serbian_custom_analyzer")
    private String description;

    @Field(type = FieldType.Text, analyzer = "serbian_custom_analyzer")
    private String fileDescription;

    @Field(type = FieldType.Integer)
    private Integer reviewCount;

    @Field(type = FieldType.Float)
    private Float avgPerformanceGrade;

    @Field(type = FieldType.Float)
    private Float avgSoundGrade;

    @Field(type = FieldType.Float)
    private Float avgLightingGrade;

    @Field(type = FieldType.Float)
    private Float avgSpaceGrade;

    @Field(type = FieldType.Float)
    private Float avgExperienceGrade;

    public LocationIndex() {
    }

    public LocationIndex(Long id, String name, String description, String fileDescription, Integer reviewCount, Float avgPerformanceGrade, Float avgSoundGrade, Float avgLightingGrade, Float avgSpaceGrade, Float avgExperienceGrade) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fileDescription = fileDescription;
        this.reviewCount = reviewCount;
        this.avgPerformanceGrade = avgPerformanceGrade;
        this.avgSoundGrade = avgSoundGrade;
        this.avgLightingGrade = avgLightingGrade;
        this.avgSpaceGrade = avgSpaceGrade;
        this.avgExperienceGrade = avgExperienceGrade;
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

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
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
}
