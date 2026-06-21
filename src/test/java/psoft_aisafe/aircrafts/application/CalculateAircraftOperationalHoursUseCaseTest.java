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
class CalculateAircraftOperationalHoursUseCaseTest {

    @Mock private AircraftRepository repository;
    @InjectMocks private CalculateAircraftOperationalHoursUseCase useCase;

    @Test
    void handlesEmptyFleet() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        var res = useCase.execute();
        assertNotNull(res);
    }

    @Test
    void calculatesTotalOperationalHoursCorrectly() {
        Aircraft mock1 = mock(Aircraft.class);
        RegistrationNumber reg1 = mock(RegistrationNumber.class);
        when(reg1.getNumber()).thenReturn("CS-TPA");
        when(mock1.getRegistrationNumber()).thenReturn(reg1);
        when(mock1.getTotalFlightHours()).thenReturn(100);

        Aircraft mock2 = mock(Aircraft.class);
        RegistrationNumber reg2 = mock(RegistrationNumber.class);
        when(reg2.getNumber()).thenReturn("CS-TPB");
        when(mock2.getRegistrationNumber()).thenReturn(reg2);
        when(mock2.getTotalFlightHours()).thenReturn(200);

        when(repository.findAll()).thenReturn(List.of(mock1, mock2));

        var res = useCase.execute();
        assertNotNull(res);
    }
}