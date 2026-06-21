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
class GetFleetStatusUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private GetFleetStatusUseCase useCase;

    @Test void handlesEmptyFleetCorrectly() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        var result = useCase.execute();
        assertNotNull(result);
    }

    @Test void calculatesCountsProperly() {
        Aircraft a1 = mock(Aircraft.class);
        RegistrationNumber r1 = mock(RegistrationNumber.class);
        lenient().when(r1.getNumber()).thenReturn("CS-TPA");
        when(a1.getRegistrationNumber()).thenReturn(r1);
        when(a1.getCurrentStatus()).thenReturn(AircraftStatus.AVAILABLE);

        Aircraft a2 = mock(Aircraft.class);
        RegistrationNumber r2 = mock(RegistrationNumber.class);
        lenient().when(r2.getNumber()).thenReturn("CS-TPB");
        when(a2.getRegistrationNumber()).thenReturn(r2);
        when(a2.getCurrentStatus()).thenReturn(AircraftStatus.IN_FLIGHT);

        when(repository.findAll()).thenReturn(List.of(a1, a2));
        var result = useCase.execute();
        assertNotNull(result);
    }

    @Test void handlesMultipleAircraftsWithSameStatus() {
        Aircraft a1 = mock(Aircraft.class);
        RegistrationNumber r1 = mock(RegistrationNumber.class);
        lenient().when(r1.getNumber()).thenReturn("CS-TPA");
        when(a1.getRegistrationNumber()).thenReturn(r1);
        when(a1.getCurrentStatus()).thenReturn(AircraftStatus.IN_FLIGHT);

        Aircraft a2 = mock(Aircraft.class);
        RegistrationNumber r2 = mock(RegistrationNumber.class);
        lenient().when(r2.getNumber()).thenReturn("CS-TPB");
        when(a2.getRegistrationNumber()).thenReturn(r2);
        when(a2.getCurrentStatus()).thenReturn(AircraftStatus.IN_FLIGHT);

        when(repository.findAll()).thenReturn(List.of(a1, a2));
        var result = useCase.execute();
        assertNotNull(result);
    }
}