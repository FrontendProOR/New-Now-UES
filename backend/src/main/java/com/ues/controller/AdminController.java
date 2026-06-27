package com.ues.controller;

import com.ues.dto.AccountRequestDto;
import com.ues.dto.ManagerDto;
import com.ues.service.AccountRequestService;
import com.ues.service.ManagesService;
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
    private final ManagesService managesService;

    public AdminController(AccountRequestService accountRequestService,
                           ManagesService managesService) {
        this.accountRequestService = accountRequestService;
        this.managesService = managesService;
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

    @GetMapping("/locations/{locationId}/managers")
    public ResponseEntity<List<ManagerDto>> getManagers(@PathVariable Long locationId) {
        logger.info("Admin fetching managers for location id={}", locationId);
        return ResponseEntity.ok(managesService.getManagersForLocation(locationId));
    }

    @PostMapping("/locations/{locationId}/managers")
    public ResponseEntity<String> assignManager(@PathVariable Long locationId,
                                                 @RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        logger.info("Admin assigning user email={} as manager of location id={}", email, locationId);
        managesService.assignManager(locationId, email);
        return ResponseEntity.ok("Manager assigned successfully");
    }

    @DeleteMapping("/locations/{locationId}/managers/{userId}")
    public ResponseEntity<Void> removeManager(@PathVariable Long locationId,
                                               @PathVariable Long userId) {
        logger.info("Admin removing user id={} from managing location id={}", userId, locationId);
        managesService.removeManager(locationId, userId);
        return ResponseEntity.noContent().build();
    }
}
