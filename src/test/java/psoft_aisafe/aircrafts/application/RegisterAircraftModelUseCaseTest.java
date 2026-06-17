package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAircraftModelUseCaseTest {

    @Mock private AircraftModelRepository repository;
    @InjectMocks private RegisterAircraftModelUseCase useCase;

    @Test
    void shouldRegisterModelWithUploadedFileSuccessfully() {
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B777", 15000, 10000, 900);
        MockMultipartFile file = new MockMultipartFile("file", "teste.png", "image/png", "dados".getBytes());

        when(repository.findByModelName("B777")).thenReturn(Optional.empty());
        when(repository.save(any(AircraftModel.class))).thenAnswer(i -> i.getArgument(0));

        AircraftModelResponse response = useCase.execute(request, file);
        assertEquals("http://localhost:8080/diagrams/b777.png", response.technicalDiagramUrl());
    }

    @Test
    void shouldReturnEmptyStringIfDiagramIsMissing() {
        RegisterAircraftModelRequest request = new RegisterAircraftModelRequest(AircraftManufacturer.AIRBUS, "A320", 15000, 10000, 900);

        when(repository.findByModelName("A320")).thenReturn(Optional.empty());
        when(repository.save(any(AircraftModel.class))).thenAnswer(i -> i.getArgument(0));

        AircraftModelResponse response = useCase.execute(request, null);
        assertEquals("", response.technicalDiagramUrl());
    }
}