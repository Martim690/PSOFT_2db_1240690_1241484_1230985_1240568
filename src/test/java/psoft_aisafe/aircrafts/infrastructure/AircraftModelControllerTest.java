package psoft_aisafe.aircrafts.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import psoft_aisafe.aircrafts.application.GetTopUtilizedModelsUseCase;
import psoft_aisafe.aircrafts.application.ListAircraftModelsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftModelUseCase;
import psoft_aisafe.aircrafts.application.UpdateAircraftModelSpecsUseCase;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AircraftModelControllerTest {

    @Mock private RegisterAircraftModelUseCase registerUseCase;
    @Mock private ListAircraftModelsUseCase listUseCase;
    @Mock private UpdateAircraftModelSpecsUseCase updateUseCase;
    @Mock private GetTopUtilizedModelsUseCase topUtilizedUseCase;

    @InjectMocks private AircraftModelController controller;

    @Test
    void shouldReturn201CreatedWhenRegisteringModel() {
        // Correção do 6.º parâmetro adicionando "url.png"
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B777", 100, 100, 100, "url.png");

        // Retornamos agora o DTO esperado e não a Entidade
        AircraftModelResponse expectedResponse = new AircraftModelResponse("B777", "BOEING", 100, 100, 100, "url.png");

        when(registerUseCase.execute(any(RegisterAircraftModelRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = controller.registerModel(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}