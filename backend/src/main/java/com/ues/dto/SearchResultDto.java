package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
