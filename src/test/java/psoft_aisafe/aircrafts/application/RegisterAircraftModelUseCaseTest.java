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
        // Arrange
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800);

        // Act
        AircraftModel result = useCase.execute(request);

        // Assert
        assertNotNull(result);
        assertTrue(modelRepository.findByModelName("B737").isPresent());
    }

    @Test
    void shouldThrowExceptionIfModelAlreadyExistsInDatabase() {
        // Arrange
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800);
        useCase.execute(request); // Save the first one

        // Act & Assert: Try to save a duplicate
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
    }
}