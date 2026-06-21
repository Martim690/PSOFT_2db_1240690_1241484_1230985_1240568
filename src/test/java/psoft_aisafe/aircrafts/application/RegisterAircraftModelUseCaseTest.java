package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAircraftModelUseCaseTest {

    @Mock private AircraftModelRepository repository;
    @InjectMocks private RegisterAircraftModelUseCase useCase;

    @Test
    void throwsExceptionWhenModelNameAlreadyExists() {
        when(repository.findByModelName("B737")).thenReturn(Optional.of(mock(AircraftModel.class)));
        RegisterAircraftModelRequest req = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 1000, 2000, 500);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(req, null));
    }

    @Test
    void registersModelSuccessfullyWithoutFile() {
        when(repository.findByModelName("B737")).thenReturn(Optional.empty());
        AircraftModel savedMock = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "empty.png");
        when(repository.save(any(AircraftModel.class))).thenReturn(savedMock);
        RegisterAircraftModelRequest req = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 1000, 2000, 500);
        AircraftModelResponse res = useCase.execute(req, null);
        assertEquals("B737", res.modelName());
        assertEquals("BOEING", res.manufacturer());
    }

    @Test
    void handlesEmptyMultipartFile() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);
        when(repository.findByModelName("B737")).thenReturn(Optional.empty());
        AircraftModel savedMock = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "empty.png");
        when(repository.save(any(AircraftModel.class))).thenReturn(savedMock);
        RegisterAircraftModelRequest req = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 1000, 2000, 500);
        AircraftModelResponse res = useCase.execute(req, mockFile);
        assertEquals("B737", res.modelName());
    }
}