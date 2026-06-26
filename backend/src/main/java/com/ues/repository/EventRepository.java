package com.ues.repository;

import com.ues.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByLocationId(Long locationId);

    List<Event> findByDate(LocalDate date);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.name = :name AND e.type = :type " +
           "AND e.location.id = :locationId AND e.date <= :beforeDate")
    int countEventOccurrences(@Param("name") String name,
                              @Param("type") String type,
                              @Param("locationId") Long locationId,
                              @Param("beforeDate") LocalDate beforeDate);
}
