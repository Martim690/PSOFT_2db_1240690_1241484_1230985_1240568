package psoft_aisafe.aircrafts.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    // Procura um avião usando o Value Object da Matrícula
    Optional<Aircraft> findByRegistrationNumber(RegistrationNumber registrationNumber);
}