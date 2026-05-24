package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegisterAircraftModelUseCaseTest {

    @Autowired private RegisterAircraftModelUseCase useCase;
    @Autowired private AircraftModelRepository modelRepository;

    @Test
    void shouldRegisterModelSuccessfullyInDatabase() {
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800);

        AircraftModel result = useCase.execute(request);

        assertNotNull(result);
        assertTrue(modelRepository.findByModelName("B737").isPresent());
    }

    @Test
    void shouldThrowExceptionIfModelAlreadyExistsInDatabase() {
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800);
        useCase.execute(request);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
    }
}