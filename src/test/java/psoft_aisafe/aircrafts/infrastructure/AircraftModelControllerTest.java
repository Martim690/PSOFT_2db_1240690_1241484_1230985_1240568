package psoft_aisafe.aircrafts.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import psoft_aisafe.aircrafts.application.RegisterAircraftModelUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.ListAircraftModelsUseCase;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.security.application.JwtService;
import psoft_aisafe.security.domain.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftModelController.class)
@AutoConfigureMockMvc(addFilters = false)
class AircraftModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private RegisterAircraftModelUseCase registerAircraftModelUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ListAircraftModelsUseCase listAircraftModelsUseCase;

    @Test
    void ensureRegisterModelReturns201() throws Exception {
        // Arrange: Payload exatamente como o teu record RegisterAircraftModelRequest espera
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(
                AircraftManufacturer.AIRBUS, "A320", 20000, 5000, 800);

        // Arrange: Entidade que o UseCase retorna
        AircraftModel response = new AircraftModel("A320", 20000, 5000, 800, AircraftManufacturer.AIRBUS);

        when(registerAircraftModelUseCase.execute(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/aircraft-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.modelName").value("A320"))
                .andExpect(jsonPath("$.manufacturer").value("AIRBUS"));
    }

    @Test
    void ensureRegisterModelWithInvalidDataReturns400() throws Exception {
        // Arrange: Exemplo de dados inválidos (ex: range negativo)
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(
                AircraftManufacturer.AIRBUS, "A320", 20000, -1, 800);

        mockMvc.perform(post("/api/aircraft-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}