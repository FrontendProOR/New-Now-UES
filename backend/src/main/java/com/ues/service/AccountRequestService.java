package com.ues.service;

import com.ues.dto.AccountRequestDto;
import com.ues.model.AccountRequest;
import com.ues.model.RequestStatus;
import com.ues.model.User;
import com.ues.repository.AccountRequestRepository;
import com.ues.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountRequestService {

    private static final Logger logger = LogManager.getLogger(AccountRequestService.class);

    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AccountRequestService(AccountRequestRepository accountRequestRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  EmailService emailService) {
        this.accountRequestRepository = accountRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public AccountRequest createRequest(String email, String password, String address) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }
        if (accountRequestRepository.existsByEmailAndStatus(email, RequestStatus.PENDING)) {
            throw new IllegalArgumentException("A pending request already exists for this email");
        }

        AccountRequest request = new AccountRequest();
        request.setEmail(email.toLowerCase());
        request.setPassword(passwordEncoder.encode(password));
        request.setAddress(address);

        AccountRequest saved = accountRequestRepository.save(request);
        logger.info("Account request created for email: {}", email);
        return saved;
    }

    public List<AccountRequestDto> getAllRequests() {
        return accountRequestRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<AccountRequestDto> getPendingRequests() {
        return accountRequestRepository.findByStatus(RequestStatus.PENDING).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AccountRequestDto acceptRequest(Long id) {
        AccountRequest request = accountRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request has already been processed");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        accountRequestRepository.save(request);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAddress(request.getAddress());
        user.setRole("ROLE_USER");
        userRepository.save(user);

        logger.info("Account request accepted for email: {}", request.getEmail());
        emailService.sendAccountApproved(request.getEmail());

        return toDto(request);
    }

    @Transactional
    public AccountRequestDto rejectRequest(Long id, String reason) {
        AccountRequest request = accountRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request has already been processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        accountRequestRepository.save(request);

        logger.info("Account request rejected for email: {}", request.getEmail());
        emailService.sendAccountRejected(request.getEmail(), reason);

        return toDto(request);
    }

    private AccountRequestDto toDto(AccountRequest request) {
        return new AccountRequestDto(
                request.getId(),
                request.getEmail(),
                request.getAddress(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRejectionReason()
        );
    }
}
