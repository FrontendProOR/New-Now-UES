package com.ues.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {

    @Min(1) @Max(10)
    private Integer performance;

    @Min(1) @Max(10)
    private Integer soundAndLighting;

    @Min(1) @Max(10)
    private Integer venue;

    @Min(1) @Max(10)
    private Integer overallImpression;
}
