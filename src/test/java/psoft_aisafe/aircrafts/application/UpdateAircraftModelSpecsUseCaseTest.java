package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftModelSpecsRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAircraftModelSpecsUseCaseTest {

    @Mock private AircraftModelRepository repository;
    @InjectMocks private UpdateAircraftModelSpecsUseCase useCase;

    @Test
    void throwsExceptionWhenModelNotFound() {
        when(repository.findByModelName("B737")).thenReturn(Optional.empty());
        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(1000, 2000, 500);
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("B737", req));
    }

    @Test
    void updatesAllFieldsSuccessfully() {
        AircraftModel existing = new AircraftModel("B737", 100, 200, 50, AircraftManufacturer.BOEING, "img.png");
        when(repository.findByModelName("B737")).thenReturn(Optional.of(existing));
        when(repository.save(any(AircraftModel.class))).thenReturn(existing);

        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(1000, 2000, 500);
        AircraftModelResponse res = useCase.execute("B737", req);

        assertEquals(1000, res.fuelCapacity());
        assertEquals(2000, res.maximumRange());
        assertEquals(500, res.cruisingSpeed());
    }

    @Test
    void keepsOldFuelCapacityIfRequestIsNull() {
        AircraftModel existing = new AircraftModel("B737", 100, 200, 50, AircraftManufacturer.BOEING, "img.png");
        when(repository.findByModelName("B737")).thenReturn(Optional.of(existing));
        when(repository.save(any(AircraftModel.class))).thenReturn(existing);

        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(null, 2000, 500);
        AircraftModelResponse res = useCase.execute("B737", req);

        assertEquals(100, res.fuelCapacity());
        assertEquals(2000, res.maximumRange());
    }

    @Test
    void keepsOldMaximumRangeIfRequestIsNull() {
        AircraftModel existing = new AircraftModel("B737", 100, 200, 50, AircraftManufacturer.BOEING, "img.png");
        when(repository.findByModelName("B737")).thenReturn(Optional.of(existing));
        when(repository.save(any(AircraftModel.class))).thenReturn(existing);

        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(1000, null, 500);
        AircraftModelResponse res = useCase.execute("B737", req);

        assertEquals(200, res.maximumRange());
        assertEquals(1000, res.fuelCapacity());
    }

    @Test
    void keepsOldCruisingSpeedIfRequestIsNull() {
        AircraftModel existing = new AircraftModel("B737", 100, 200, 50, AircraftManufacturer.BOEING, "img.png");
        when(repository.findByModelName("B737")).thenReturn(Optional.of(existing));
        when(repository.save(any(AircraftModel.class))).thenReturn(existing);

        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(1000, 2000, null);
        AircraftModelResponse res = useCase.execute("B737", req);

        assertEquals(50, res.cruisingSpeed());
    }
}