package psoft_aisafe.aircrafts.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByRegistrationNumber(RegistrationNumber registrationNumber);

    @Query("SELECT a FROM Aircraft a " +
            "WHERE (:modelName IS NULL OR a.model.modelName = :modelName) " +
            "AND (:status IS NULL OR a.currentStatus = :status) " +
            "AND (:year IS NULL OR YEAR(a.manufacturingDate) = :year)")
    List<Aircraft> searchAircrafts(@Param("modelName") String modelName,
                                   @Param("status") AircraftStatus status,
                                   @Param("year") Integer year);
}