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
class ListAircraftsUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private ListAircraftsUseCase useCase;

    @Test void returnsEmptyListWhenNoAircrafts() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(useCase.execute().isEmpty());
    }

    @Test void returnsListOfAircraftsWhenTheyExist() {
        Aircraft mockAircraft = mock(Aircraft.class);
        RegistrationNumber mockReg = mock(RegistrationNumber.class);
        AircraftModel mockModel = mock(AircraftModel.class);
        when(mockAircraft.getRegistrationNumber()).thenReturn(mockReg);
        when(mockReg.getNumber()).thenReturn("CS-TPA");
        when(mockAircraft.getModel()).thenReturn(mockModel);
        when(mockModel.getModelName()).thenReturn("B737");
        when(mockAircraft.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);
        when(repository.findAll()).thenReturn(List.of(mockAircraft));
        var result = useCase.execute();
        assertEquals(1, result.size());
    }

    @Test void callsRepositoryFindAll() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        useCase.execute();
        verify(repository, times(1)).findAll();
    }
}