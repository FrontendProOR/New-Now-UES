package com.ues.dto;

import java.time.LocalDate;

public class ManagerDto {

    private Long userId;
    private String email;
    private String name;
    private LocalDate startDate;

    public ManagerDto() {
    }

    public ManagerDto(Long userId, String email, String name, LocalDate startDate) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.startDate = startDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
