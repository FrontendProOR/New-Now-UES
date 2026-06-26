package com.ues.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "locations")
@Setting(settingPath = "/elasticsearch/settings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
