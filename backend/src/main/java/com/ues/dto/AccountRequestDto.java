package com.ues.dto;

import com.ues.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {

    private Long id;
    private String email;
    private String address;
    private RequestStatus status;
    private LocalDate createdAt;
    private String rejectionReason;
}
