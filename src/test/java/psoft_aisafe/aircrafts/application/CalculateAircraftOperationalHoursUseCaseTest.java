package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.AircraftOperationalHoursResponse;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateAircraftOperationalHoursUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @InjectMocks private CalculateAircraftOperationalHoursUseCase useCase;

    @Test
    void shouldReturnOperationalHoursForEachAircraft() {
        AircraftModel model = new AircraftModel("M1", 1, 1, 1, AircraftManufacturer.BOEING, null);
        Aircraft a1 = new Aircraft(new RegistrationNumber("CS-AAA"), model, LocalDate.now(), 100, AircraftStatus.AVAILABLE, 1250, 100);

        when(aircraftRepository.findAll()).thenReturn(List.of(a1));

        List<AircraftOperationalHoursResponse> result = useCase.execute();

        assertEquals(1, result.size());
        assertEquals("CS-AAA", result.get(0).registrationNumber());
        assertEquals(1250, result.get(0).totalFlightHours());
    }
}