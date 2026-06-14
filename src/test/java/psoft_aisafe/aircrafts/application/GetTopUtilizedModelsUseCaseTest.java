package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.TopUtilizedModelResponse;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTopUtilizedModelsUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @InjectMocks private GetTopUtilizedModelsUseCase useCase;

    @Test
    void shouldReturnTopUtilizedSortedByFlightHours() {
        AircraftModel m1 = new AircraftModel("M1", 1, 1, 1, AircraftManufacturer.BOEING, null);
        AircraftModel m2 = new AircraftModel("M2", 1, 1, 1, AircraftManufacturer.AIRBUS, null);

        Aircraft a1 = new Aircraft(new RegistrationNumber("CS-AAA"), m1, LocalDate.now(), 100, AircraftStatus.AVAILABLE, 100, 5);
        Aircraft a2 = new Aircraft(new RegistrationNumber("CS-BBB"), m2, LocalDate.now(), 100, AircraftStatus.AVAILABLE, 500, 1);

        when(aircraftRepository.findAll()).thenReturn(List.of(a1, a2));

        List<TopUtilizedModelResponse> result = useCase.execute("hours");

        // O M2 tem 500 horas, por isso tem de vir em primeiro
        assertEquals("M2", result.get(0).modelName());
        assertEquals(500, result.get(0).totalFlightHours());
        assertEquals("M1", result.get(1).modelName());
    }
}