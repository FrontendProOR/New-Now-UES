package com.ues.repository;

import com.ues.model.AccountRequest;
import com.ues.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

    Optional<AccountRequest> findByEmail(String email);

    List<AccountRequest> findByStatus(RequestStatus status);

    boolean existsByEmailAndStatus(String email, RequestStatus status);
}
