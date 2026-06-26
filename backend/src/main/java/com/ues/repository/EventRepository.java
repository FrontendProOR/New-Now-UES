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

    @Query("SELECT e FROM Event e WHERE " +
           "(:type IS NULL OR LOWER(e.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
           "(:locationId IS NULL OR e.location.id = :locationId) AND " +
           "(:address IS NULL OR LOWER(e.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
           "(:minPrice IS NULL OR e.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR e.price <= :maxPrice) AND " +
           "(:dateFrom IS NULL OR e.date >= :dateFrom) AND " +
           "(:dateTo IS NULL OR e.date <= :dateTo)")
    List<Event> searchEvents(@Param("type") String type,
                             @Param("locationId") Long locationId,
                             @Param("address") String address,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice,
                             @Param("dateFrom") LocalDate dateFrom,
                             @Param("dateTo") LocalDate dateTo);

    @Query("SELECT e FROM Event e WHERE e.location.id = :locationId " +
           "AND e.date >= :dateFrom AND e.date <= :dateTo")
    List<Event> findByLocationIdAndDateBetween(@Param("locationId") Long locationId,
                                               @Param("dateFrom") LocalDate dateFrom,
                                               @Param("dateTo") LocalDate dateTo);
}
