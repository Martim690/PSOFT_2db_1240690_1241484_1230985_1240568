package psoft_aisafe.aircrafts.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import psoft_aisafe.aircrafts.application.*;
import psoft_aisafe.aircrafts.application.dtos.*;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AircraftControllerTest {

    @Mock private RegisterAircraftUseCase registerAircraftUseCase;
    @Mock private ListAircraftsUseCase listAircraftsUseCase;
    @Mock private GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase;
    @Mock private UpdateAircraftStatusUseCase updateAircraftStatusUseCase;
    @Mock private SearchAircraftsUseCase searchAircraftsUseCase;
    @Mock private GetCompatibleRoutesUseCase getCompatibleRoutesUseCase;
    @Mock private GetFleetStatusUseCase getFleetStatusUseCase;
    @Mock private CalculateAircraftOperationalHoursUseCase calculateAircraftOperationalHoursUseCase;

    @InjectMocks
    private AircraftController controller;

    @Test
    void shouldReturn201CreatedWhenRegisteringAircraft() {
        RegisterAircraftRequest request = new RegisterAircraftRequest("CS-TPA", "B737", LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        AircraftModel model = new AircraftModel("B737", 1000, 1000, 800, AircraftManufacturer.BOEING, null);
        Aircraft expectedAircraft = new Aircraft(new RegistrationNumber("CS-TPA"), model, LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        when(registerAircraftUseCase.execute(any(RegisterAircraftRequest.class))).thenReturn(expectedAircraft);

        ResponseEntity<EntityModel<Aircraft>> response = controller.registerAircraft(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("CS-TPA", response.getBody().getContent().getRegistrationNumber().getNumber());
    }

    @Test
    void shouldReturn200OkWhenListingFleetStatus() {
        FleetStatusResponse expectedResponse = new FleetStatusResponse(10, Map.of("AVAILABLE", 10L));
        when(getFleetStatusUseCase.execute()).thenReturn(expectedResponse);

        ResponseEntity<EntityModel<FleetStatusResponse>> response = controller.getFleetStatus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getContent().totalAircraft());
    }

    @Test
    void shouldReturn200OkWhenCalculatingOperationalHours() {
        AircraftOperationalHoursResponse hoursResponse = new AircraftOperationalHoursResponse("CS-TPA", 5000);
        when(calculateAircraftOperationalHoursUseCase.execute()).thenReturn(List.of(hoursResponse));

        ResponseEntity<CollectionModel<EntityModel<AircraftOperationalHoursResponse>>> response = controller.getOperationalHours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200OkWhenGettingCompatibleRoutes() {
        CompatibleRouteResponse routeResponse = new CompatibleRouteResponse("id", "LIS", "OPO", 400, 100, true);
        when(getCompatibleRoutesUseCase.execute("CS-TPA")).thenReturn(List.of(routeResponse));

        ResponseEntity<CollectionModel<EntityModel<CompatibleRouteResponse>>> response = controller.getCompatibleRoutes("CS-TPA");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}