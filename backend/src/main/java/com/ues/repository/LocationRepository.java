package com.ues.repository;

import com.ues.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findTop5ByOrderByTotalRatingDesc();

    @Query("SELECT l FROM Location l WHERE " +
           "(:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:address IS NULL OR LOWER(l.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
           "(:type IS NULL OR LOWER(l.type) LIKE LOWER(CONCAT('%', :type, '%')))")
    List<Location> searchLocations(@Param("name") String name,
                                   @Param("address") String address,
                                   @Param("type") String type);
}
