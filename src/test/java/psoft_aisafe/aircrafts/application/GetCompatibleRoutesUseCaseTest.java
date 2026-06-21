package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.CompatibleRouteResponse;
import psoft_aisafe.aircrafts.domain.*;
import psoft_aisafe.routes.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCompatibleRoutesUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @Mock private RouteRepository routeRepository;
    @InjectMocks private GetCompatibleRoutesUseCase useCase;

    @Test
    void throwsExceptionWhenAircraftNotFound() {
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("CS-ZZZ"));
    }

    @Test
    void returnsEmptyListWhenNoRoutesExist() {
        Aircraft mockAircraft = mock(Aircraft.class);
        AircraftModel mockModel = mock(AircraftModel.class);
        when(mockAircraft.getModel()).thenReturn(mockModel);
        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(mockAircraft));
        when(routeRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(useCase.execute("CS-TPA").isEmpty());
    }

    @Test
    void throwsExceptionWhenRegistrationNumberIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(""));
    }
}