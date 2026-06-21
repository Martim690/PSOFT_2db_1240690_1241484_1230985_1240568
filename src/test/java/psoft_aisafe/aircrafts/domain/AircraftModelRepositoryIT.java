package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AircraftModelRepositoryIT {

    @Autowired
    private AircraftModelRepository repository;

    @Test
    void shouldSaveAndRetrieveAircraftModelFromDatabase() {
        AircraftModel model = new AircraftModel("A321-NEO", 80000, 7000, 850, AircraftManufacturer.AIRBUS, "a321.png");
        repository.save(model);
        Optional<AircraftModel> retrieved = repository.findByModelName("A321-NEO");
        assertTrue(retrieved.isPresent());
        assertEquals(80000, retrieved.get().getFuelCapacity());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        Optional<AircraftModel> retrieved = repository.findByModelName("UNKNOWN");
        assertTrue(retrieved.isEmpty());
    }
}