package com.ues.controller;

import com.ues.dto.AccountRequestDto;
import com.ues.service.AccountRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    private final AccountRequestService accountRequestService;

    public AdminController(AccountRequestService accountRequestService) {
        this.accountRequestService = accountRequestService;
    }

    @GetMapping("/account-requests")
    public ResponseEntity<List<AccountRequestDto>> getAccountRequests() {
        logger.info("Admin fetching all account requests");
        return ResponseEntity.ok(accountRequestService.getAllRequests());
    }

    @GetMapping("/account-requests/pending")
    public ResponseEntity<List<AccountRequestDto>> getPendingRequests() {
        logger.info("Admin fetching pending account requests");
        return ResponseEntity.ok(accountRequestService.getPendingRequests());
    }

    @PutMapping("/account-requests/{id}/accept")
    public ResponseEntity<AccountRequestDto> acceptRequest(@PathVariable Long id) {
        logger.info("Admin accepting account request id: {}", id);
        return ResponseEntity.ok(accountRequestService.acceptRequest(id));
    }

    @PutMapping("/account-requests/{id}/reject")
    public ResponseEntity<AccountRequestDto> rejectRequest(@PathVariable Long id,
                                                            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        logger.info("Admin rejecting account request id: {} with reason: {}", id, reason);
        return ResponseEntity.ok(accountRequestService.rejectRequest(id, reason));
    }
}
