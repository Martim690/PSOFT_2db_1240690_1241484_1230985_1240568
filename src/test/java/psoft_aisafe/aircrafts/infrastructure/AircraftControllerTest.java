package psoft_aisafe.aircrafts.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import psoft_aisafe.aircrafts.application.*;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.*;
import psoft_aisafe.security.application.JwtService;
import psoft_aisafe.security.domain.UserRepository;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftController.class)
@AutoConfigureMockMvc(addFilters = false)
class AircraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase;

    @MockitoBean
    private ListAircraftsUseCase listAircraftsUseCase;

    @MockitoBean
    private RegisterAircraftUseCase registerAircraftUseCase;

    @MockitoBean
    private SearchAircraftsUseCase searchAircraftsUseCase;

    @MockitoBean
    private UpdateAircraftStatusUseCase updateAircraftStatusUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    private Aircraft mockAircraft;

    @BeforeEach
    void setUp() {
        AircraftModel model = new AircraftModel("A320", 20000, 5000, 800, AircraftManufacturer.AIRBUS);
        mockAircraft = new Aircraft(new RegistrationNumber("CS-TPA"), model, LocalDate.now(), 150, AircraftStatus.AVAILABLE);
    }

    @Test
    void ensureRegisterAircraftReturns201Created() throws Exception {
        RegisterAircraftRequest request = new RegisterAircraftRequest("CS-TPA", "A320", LocalDate.now(), 150, AircraftStatus.AVAILABLE);

        when(registerAircraftUseCase.execute(any())).thenReturn(mockAircraft);

        mockMvc.perform(post("/api/aircrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber.number").value("CS-TPA"));
    }

    @Test
    void ensureGetAircraftByRegistrationReturns200OK() throws Exception {
        when(getAircraftByRegistrationUseCase.execute("CS-TPA")).thenReturn(mockAircraft);

        mockMvc.perform(get("/api/aircrafts/CS-TPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber.number").value("CS-TPA"));
    }

    @Test
    void ensureUpdateStatusWithIfMatchHeaderReturns200OK() throws Exception {
        UpdateAircraftStatusRequest request = new UpdateAircraftStatusRequest(AircraftStatus.INACTIVE);

        when(updateAircraftStatusUseCase.execute(any(), any())).thenReturn(mockAircraft);

        mockMvc.perform(patch("/api/aircrafts/CS-TPA/status")
                        .header("If-Match", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber.number").value("CS-TPA"));
    }
}