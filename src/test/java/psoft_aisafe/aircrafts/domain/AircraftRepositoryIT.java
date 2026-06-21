package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AircraftRepositoryIT {

    @Autowired private AircraftRepository aircraftRepository;
    @Autowired private AircraftModelRepository modelRepository;

    private AircraftModel savedModel;

    @BeforeEach
    void setUp() {
        AircraftModel model = new AircraftModel("A320", 20000, 5000, 800, AircraftManufacturer.AIRBUS, "a320.png");
        savedModel = modelRepository.save(model);
    }

    @Test
    void shouldSaveAndFindAircraftByRegistrationNumber() {
        RegistrationNumber reg = new RegistrationNumber("CS-TPA");
        Aircraft aircraft = new Aircraft(reg, savedModel, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        aircraftRepository.save(aircraft);
        Optional<Aircraft> found = aircraftRepository.findByRegistrationNumber(reg);

        assertTrue(found.isPresent());
        assertEquals("CS-TPA", found.get().getRegistrationNumber().getNumber());
        assertEquals(AircraftStatus.AVAILABLE, found.get().getCurrentStatus());
        assertEquals("A320", found.get().getModel().getModelName());
    }

    @Test
    void shouldReturnEmptyWhenRegistrationNumberDoesNotExist() {
        RegistrationNumber reg = new RegistrationNumber("CS-ZZZ");
        Optional<Aircraft> found = aircraftRepository.findByRegistrationNumber(reg);

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindAllAircrafts() {
        RegistrationNumber reg1 = new RegistrationNumber("CS-TPA");
        Aircraft a1 = new Aircraft(reg1, savedModel, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        RegistrationNumber reg2 = new RegistrationNumber("CS-TPB");
        Aircraft a2 = new Aircraft(reg2, savedModel, LocalDate.now(), 200, AircraftStatus.IN_FLIGHT);

        aircraftRepository.save(a1);
        aircraftRepository.save(a2);

        var allAircrafts = aircraftRepository.findAll();

        assertEquals(2, allAircrafts.size());
    }
}