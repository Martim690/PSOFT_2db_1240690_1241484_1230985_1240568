package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegisterAircraftUseCaseTest {

    @Autowired private RegisterAircraftUseCase aircraftUseCase;
    @Autowired private RegisterAircraftModelUseCase modelUseCase;
    @Autowired private AircraftRepository aircraftRepository;

    @Test
    void shouldRegisterAircraftSuccessfullyInDatabase() {
        modelUseCase.execute(new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800,null));
        RegisterAircraftRequest request = new RegisterAircraftRequest("CS-REG1", "B737", LocalDate.now(), 180, AircraftStatus.AVAILABLE);

        Aircraft result = aircraftUseCase.execute(request);

        assertNotNull(result);
        assertTrue(aircraftRepository.findByRegistrationNumber(new RegistrationNumber("CS-REG1")).isPresent());
    }

    @Test
    void shouldFailIfModelDoesNotExistInDatabase() {
        RegisterAircraftRequest request = new RegisterAircraftRequest("CS-TPB", "FAKE_MODEL", LocalDate.now(), 180, AircraftStatus.AVAILABLE);

        assertThrows(IllegalArgumentException.class, () -> aircraftUseCase.execute(request));
    }
}