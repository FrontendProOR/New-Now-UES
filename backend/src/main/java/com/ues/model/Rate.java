package com.ues.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(mappedBy = "rate")
    private Review review;
}
