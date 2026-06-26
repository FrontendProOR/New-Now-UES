package com.ues.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long authorId;
    private String authorName;
    private String authorRole;
    private Long reviewId;
    private Long parentId;
    private List<CommentDto> replies = new ArrayList<>();
}
