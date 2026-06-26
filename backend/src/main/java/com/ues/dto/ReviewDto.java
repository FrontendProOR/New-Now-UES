package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private LocalDateTime createdAt;
    private Integer eventCount;
    private Boolean hidden;

    private Long authorId;
    private String authorName;

    private Long locationId;
    private String locationName;

    private Long eventId;
    private String eventName;

    private RateDto rate;
    private String rootComment;
}
