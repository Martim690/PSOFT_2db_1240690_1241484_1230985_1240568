package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftModelSpecsRequest;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAircraftModelSpecsUseCaseTest {

    @Mock private AircraftModelRepository repository;
    @InjectMocks private UpdateAircraftModelSpecsUseCase useCase;

    @Test
    void shouldUpdatePartialSpecificationsAndKeepOldValues() {
        AircraftModel existingModel = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        when(repository.findByModelName("B737")).thenReturn(Optional.of(existingModel));
        when(repository.save(any(AircraftModel.class))).thenReturn(existingModel);

        UpdateAircraftModelSpecsRequest request = new UpdateAircraftModelSpecsRequest(8000, null, 900);

        AircraftModel result = useCase.execute("B737", request);

        assertEquals(8000, result.getFuelCapacity());
        assertEquals(2000, result.getMaximumRange());
        assertEquals(900, result.getCruisingSpeed());
    }
}