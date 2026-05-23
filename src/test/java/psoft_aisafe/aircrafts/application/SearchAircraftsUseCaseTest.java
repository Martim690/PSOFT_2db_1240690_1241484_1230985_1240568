package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SearchAircraftsUseCaseTest {

    @Autowired private SearchAircraftsUseCase searchUseCase;
    @Autowired private RegisterAircraftUseCase registerAircraftUseCase;
    @Autowired private RegisterAircraftModelUseCase registerModelUseCase;

    @Test
    void shouldSearchAircraftsInDatabase() {
        // Arrange
        registerModelUseCase.execute(new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800));
        registerAircraftUseCase.execute(new RegisterAircraftRequest("CS-SRC1", "B737", LocalDate.now(), 180, AircraftStatus.IN_FLIGHT));
        registerAircraftUseCase.execute(new RegisterAircraftRequest("CS-SRC2", "B737", LocalDate.now(), 180, AircraftStatus.AVAILABLE));

        // Act: Search only for IN_FLIGHT aircrafts
        List<Aircraft> result = searchUseCase.execute(null, AircraftStatus.IN_FLIGHT, null);

        // Assert
        assertFalse(result.isEmpty());

        // Check if ANY aircraft in the returned list matches our CS-SRC1
        boolean found = result.stream()
                .anyMatch(a -> a.getRegistrationNumber().getNumber().equals("CS-SRC1"));
        assertTrue(found, "The test aircraft CS-SRC1 should be in the search results.");
    }
}