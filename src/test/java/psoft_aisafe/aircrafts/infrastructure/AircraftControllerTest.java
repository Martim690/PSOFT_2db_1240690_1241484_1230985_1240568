package psoft_aisafe.aircrafts.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import psoft_aisafe.aircrafts.application.*;
import psoft_aisafe.aircrafts.application.dtos.*;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AircraftControllerTest {

    @Mock private RegisterAircraftUseCase registerUseCase;
    @Mock private ListAircraftsUseCase listUseCase;
    @Mock private GetAircraftByRegistrationUseCase getByRegistrationUseCase;
    @Mock private SearchAircraftsUseCase searchUseCase;
    @Mock private UpdateAircraftStatusUseCase updateStatusUseCase;
    @Mock private GetFleetStatusUseCase getFleetStatusUseCase;
    @Mock private CalculateAircraftOperationalHoursUseCase calculateOperationalHoursUseCase;
    @Mock private GetCompatibleRoutesUseCase getCompatibleRoutesUseCase;

    @InjectMocks private AircraftController controller;

    @Test
    void shouldReturn201WhenRegisteringAircraft() {
        RegisterAircraftRequest request = new RegisterAircraftRequest("CS-TPA", "B737", LocalDate.now(), 150, AircraftStatus.AVAILABLE);
        Aircraft mockAircraft = mock(Aircraft.class);
        RegistrationNumber mockReg = mock(RegistrationNumber.class);
        AircraftModel mockModel = mock(AircraftModel.class);

        lenient().when(mockReg.getNumber()).thenReturn("CS-TPA");
        lenient().when(mockAircraft.getRegistrationNumber()).thenReturn(mockReg);
        lenient().when(mockModel.getModelName()).thenReturn("B737");
        lenient().when(mockAircraft.getModel()).thenReturn(mockModel);
        lenient().when(mockAircraft.getManufacturingDate()).thenReturn(LocalDate.now());
        lenient().when(mockAircraft.getSeatingCapacity()).thenReturn(150);
        lenient().when(mockAircraft.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);

        when(registerUseCase.execute(any(RegisterAircraftRequest.class))).thenReturn(mockAircraft);
        var response = controller.registerAircraft(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenListingAircrafts() {
        AircraftResponse expectedResponse = new AircraftResponse("CS-TPA", "B737", LocalDate.now(), 150, "AVAILABLE");
        when(listUseCase.execute()).thenReturn(List.of(expectedResponse));
        var response = controller.listAircrafts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenGettingAircraftByRegistration() {
        AircraftResponse expectedResponse = new AircraftResponse("CS-TPA", "B737", LocalDate.now(), 150, "AVAILABLE");
        when(getByRegistrationUseCase.execute("CS-TPA")).thenReturn(expectedResponse);
        var response = controller.getAircraftByRegistration("CS-TPA");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenSearchingAircrafts() {
        AircraftResponse expectedResponse = new AircraftResponse("CS-TPA", "B737", LocalDate.now(), 150, "IN_FLIGHT");
        when(searchUseCase.execute(null, AircraftStatus.IN_FLIGHT, null)).thenReturn(List.of(expectedResponse));
        var response = controller.getAircrafts(null, AircraftStatus.IN_FLIGHT, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenUpdatingStatus() {
        UpdateAircraftStatusRequest request = new UpdateAircraftStatusRequest(AircraftStatus.UNDER_MAINTENANCE);
        Aircraft mockAircraft = mock(Aircraft.class);
        RegistrationNumber mockReg = mock(RegistrationNumber.class);
        AircraftModel mockModel = mock(AircraftModel.class);

        lenient().when(mockReg.getNumber()).thenReturn("CS-TPA");
        lenient().when(mockAircraft.getRegistrationNumber()).thenReturn(mockReg);
        lenient().when(mockModel.getModelName()).thenReturn("B737");
        lenient().when(mockAircraft.getModel()).thenReturn(mockModel);
        lenient().when(mockAircraft.getManufacturingDate()).thenReturn(LocalDate.now());
        lenient().when(mockAircraft.getSeatingCapacity()).thenReturn(150);
        lenient().when(mockAircraft.getCurrentStatus()).thenReturn(AircraftStatus.UNDER_MAINTENANCE);

        when(updateStatusUseCase.execute(eq("CS-TPA"), any(UpdateAircraftStatusRequest.class))).thenReturn(mockAircraft);
        var response = controller.updateAircraftStatus("CS-TPA", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenGettingFleetStatus() {
        FleetStatusResponse expectedResponse = new FleetStatusResponse(Map.of("AVAILABLE", 1L), Collections.emptyList());
        when(getFleetStatusUseCase.execute()).thenReturn(expectedResponse);
        var response = controller.getFleetStatus();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenGettingCompatibleRoutes() {
        CompatibleRouteResponse expectedResponse = new CompatibleRouteResponse("RT1", "OPO", "LIS", 100, 150, true);
        when(getCompatibleRoutesUseCase.execute("CS-TPA")).thenReturn(List.of(expectedResponse));
        var response = controller.getCompatibleRoutes("CS-TPA");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}