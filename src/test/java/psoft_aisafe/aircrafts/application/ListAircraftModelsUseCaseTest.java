package psoft_aisafe.aircrafts.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psoft_aisafe.aircrafts.domain.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAircraftModelsUseCaseTest {

    @Mock private AircraftModelRepository repository;
    @InjectMocks private ListAircraftModelsUseCase useCase;

    @Test void returnsEmptyListWhenNoModels() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(useCase.execute().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test void returnsListOfModelsWhenTheyExist() {
        AircraftModel mockModel = mock(AircraftModel.class);
        when(mockModel.getModelName()).thenReturn("B737");
        when(mockModel.getManufacturer()).thenReturn(AircraftManufacturer.BOEING);
        when(repository.findAll()).thenReturn(List.of(mockModel));
        var result = useCase.execute();
        assertEquals(1, result.size());
    }

    @Test void returnsMultipleModels() {
        AircraftModel mock1 = mock(AircraftModel.class);
        when(mock1.getModelName()).thenReturn("B737");
        when(mock1.getManufacturer()).thenReturn(AircraftManufacturer.BOEING);
        AircraftModel mock2 = mock(AircraftModel.class);
        when(mock2.getModelName()).thenReturn("A320");
        when(mock2.getManufacturer()).thenReturn(AircraftManufacturer.AIRBUS);
        when(repository.findAll()).thenReturn(List.of(mock1, mock2));
        var result = useCase.execute();
        assertEquals(2, result.size());
    }
}