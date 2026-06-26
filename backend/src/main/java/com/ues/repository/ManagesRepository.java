package com.ues.repository;

import com.ues.model.Manages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagesRepository extends JpaRepository<Manages, Long> {

    List<Manages> findByUserId(Long userId);

    List<Manages> findByLocationId(Long locationId);

    boolean existsByUserIdAndLocationIdAndEndDateIsNull(Long userId, Long locationId);
}
