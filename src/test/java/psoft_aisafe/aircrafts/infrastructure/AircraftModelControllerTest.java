package psoft_aisafe.aircrafts.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import psoft_aisafe.aircrafts.application.*;
import psoft_aisafe.aircrafts.application.dtos.*;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AircraftModelControllerTest {

    @Mock private RegisterAircraftModelUseCase registerUseCase;
    @Mock private ListAircraftModelsUseCase listUseCase;
    @Mock private UpdateAircraftModelSpecsUseCase updateUseCase;
    @Mock private GetTopUtilizedModelsUseCase topUtilizedUseCase;

    @InjectMocks private AircraftModelController controller;

    @Test
    void shouldReturn201WhenRegisteringModel() {
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B777", 100, 100, 100);
        AircraftModelResponse expectedResponse = new AircraftModelResponse("B777", "BOEING", 100, 100, 100, "http://localhost:8080/diagrams/b777.png");
        when(registerUseCase.execute(eq(request), any())).thenReturn(expectedResponse);
        var response = controller.registerAircraftModel(request, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenListingModels() {
        AircraftModelResponse expectedResponse = new AircraftModelResponse("B777", "BOEING", 100, 100, 100, "url");
        when(listUseCase.execute()).thenReturn(List.of(expectedResponse));
        var response = controller.listModels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenListingEmptyModels() {
        when(listUseCase.execute()).thenReturn(Collections.emptyList());
        var response = controller.listModels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturn200WhenUpdatingSpecs() {
        UpdateAircraftModelSpecsRequest request = new UpdateAircraftModelSpecsRequest(200, 200, 200);
        AircraftModelResponse expectedResponse = new AircraftModelResponse("B777", "BOEING", 200, 200, 200, "url");
        when(updateUseCase.execute(eq("B777"), eq(request))).thenReturn(expectedResponse);
        var response = controller.updateSpecs("B777", request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenGettingTopUtilized() {
        TopUtilizedModelResponse expected = new TopUtilizedModelResponse("B777", "BOEING", 5000, 1);
        when(topUtilizedUseCase.execute(any())).thenReturn(List.of(expected));
        var response = controller.getTopUtilizedModels("hours");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn200WhenTopUtilizedIsEmpty() {
        when(topUtilizedUseCase.execute(any())).thenReturn(Collections.emptyList());
        var response = controller.getTopUtilizedModels("hours");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}