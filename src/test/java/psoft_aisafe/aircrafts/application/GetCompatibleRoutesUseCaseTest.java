package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.CompatibleRouteResponse;
import psoft_aisafe.aircrafts.domain.*;
import psoft_aisafe.routes.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCompatibleRoutesUseCaseTest {

    @Mock private AircraftRepository aircraftRepository;
    @Mock private RouteRepository routeRepository;
    @InjectMocks private GetCompatibleRoutesUseCase useCase;

    @Test
    void shouldReturnOnlyCompatibleAndActiveRoutes() {
        // Avião: Range 5000, Capacidade 150
        AircraftModel model = new AircraftModel("B737", 10000, 5000, 800, AircraftManufacturer.BOEING, null);
        Aircraft aircraft = new Aircraft(new RegistrationNumber("CS-TPA"), model, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        when(aircraftRepository.findByRegistrationNumber(any(RegistrationNumber.class))).thenReturn(Optional.of(aircraft));

        Route r1 = new Route("LIS", "OPO", new RouteRequirements(400, 100, "CAT1"), 300.0, 50); // Compatível
        Route r2 = new Route("LIS", "JFK", new RouteRequirements(6000, 100, "CAT1"), 300.0, 50); // Incompatível (Range é 6000)

        when(routeRepository.findAll()).thenReturn(List.of(r1, r2));

        List<CompatibleRouteResponse> result = useCase.execute("CS-TPA");

        assertEquals(1, result.size());
        assertEquals("LIS", result.get(0).originIataCode());
        assertEquals("OPO", result.get(0).destinationIataCode());
    }
}