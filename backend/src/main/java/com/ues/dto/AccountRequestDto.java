package com.ues.dto;

import com.ues.model.RequestStatus;

import java.time.LocalDate;

public class AccountRequestDto {

    private Long id;
    private String email;
    private String address;
    private RequestStatus status;
    private LocalDate createdAt;
    private String rejectionReason;

    public AccountRequestDto() {
    }

    public AccountRequestDto(Long id, String email, String address, RequestStatus status, LocalDate createdAt, String rejectionReason) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.rejectionReason = rejectionReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
