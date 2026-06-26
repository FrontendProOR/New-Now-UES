package com.ues.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public CommentDto() {
    }

    public CommentDto(Long id, String text, LocalDateTime createdAt, Long authorId, String authorName,
                      String authorRole, Long reviewId, Long parentId, List<CommentDto> replies) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorRole = authorRole;
        this.reviewId = reviewId;
        this.parentId = parentId;
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<CommentDto> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentDto> replies) {
        this.replies = replies;
    }
}
