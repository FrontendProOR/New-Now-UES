package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String name;
    private String address;
    private String type;
    private LocalDate date;
    private Double price;
    private Boolean recurrent;
    private Long locationId;
    private String locationName;
    private String imageUrl;
}
