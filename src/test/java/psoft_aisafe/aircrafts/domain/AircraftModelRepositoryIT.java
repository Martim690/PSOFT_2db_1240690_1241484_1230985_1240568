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
        assertEquals("a321.png", retrieved.get().getTechnicalDiagramUrl());
    }

    @Test
    void shouldReturnEmptyWhenModelDoesNotExistInDatabase() {
        Optional<AircraftModel> retrieved = repository.findByModelName("NON-EXISTENT");
        assertTrue(retrieved.isEmpty());
    }

    @Test
    void shouldUpdateAircraftModelInDatabase() {
        AircraftModel model = new AircraftModel("B747", 100000, 8000, 900, AircraftManufacturer.BOEING, "img.png");
        repository.save(model);

        AircraftModel dbModel = repository.findByModelName("B747").get();
        dbModel.updateSpecifications(120000, 8500, 920);
        repository.save(dbModel);

        AircraftModel updatedModel = repository.findByModelName("B747").get();
        assertEquals(120000, updatedModel.getFuelCapacity());
        assertEquals(8500, updatedModel.getMaximumRange());
    }
}