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
class GetTopUtilizedModelsUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private GetTopUtilizedModelsUseCase useCase;

    @Test void returnsEmptyListWhenNoAircrafts() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(useCase.execute("hours").isEmpty());
    }

    @Test void handlesSingleAircraftCorrectly() {
        Aircraft a1 = mock(Aircraft.class);
        AircraftModel m1 = mock(AircraftModel.class);
        when(m1.getModelName()).thenReturn("B737");
        when(m1.getManufacturer()).thenReturn(AircraftManufacturer.BOEING);
        when(a1.getModel()).thenReturn(m1);
        when(a1.getTotalFlightHours()).thenReturn(500);
        when(repository.findAll()).thenReturn(List.of(a1));
        var result = useCase.execute("hours");
        assertEquals(1, result.size());
    }

    @Test void groupsModelsAndSumsHours() {
        Aircraft a1 = mock(Aircraft.class);
        Aircraft a2 = mock(Aircraft.class);
        AircraftModel m1 = mock(AircraftModel.class);
        when(m1.getModelName()).thenReturn("B737");
        when(m1.getManufacturer()).thenReturn(AircraftManufacturer.BOEING);
        when(a1.getModel()).thenReturn(m1);
        when(a2.getModel()).thenReturn(m1);
        when(a1.getTotalFlightHours()).thenReturn(500);
        when(a2.getTotalFlightHours()).thenReturn(200);
        when(repository.findAll()).thenReturn(List.of(a1, a2));
        var result = useCase.execute("hours");
        assertEquals(1, result.size());
    }
}