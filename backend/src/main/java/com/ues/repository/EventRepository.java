package com.ues.repository;

import com.ues.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByLocationId(Long locationId);

    List<Event> findByDate(LocalDate date);
}
