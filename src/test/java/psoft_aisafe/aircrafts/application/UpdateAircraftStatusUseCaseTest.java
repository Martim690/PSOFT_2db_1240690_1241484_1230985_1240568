package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAircraftStatusUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @InjectMocks private UpdateAircraftStatusUseCase useCase;

    @Test
    void shouldUpdateStatusSuccessfully() {
        AircraftModel model = new AircraftModel("B737", 100, 100, 100, AircraftManufacturer.BOEING, null);
        Aircraft aircraft = new Aircraft(new RegistrationNumber("CS-AAA"), model, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(aircraft));
        when(aircraftRepository.save(any(Aircraft.class))).thenAnswer(i -> i.getArgument(0));

        UpdateAircraftStatusRequest request = new UpdateAircraftStatusRequest(AircraftStatus.IN_FLIGHT);

        Aircraft result = useCase.execute("CS-AAA", request);

        assertEquals(AircraftStatus.IN_FLIGHT, result.getCurrentStatus());
    }
}