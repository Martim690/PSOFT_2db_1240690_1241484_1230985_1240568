package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import psoft_aisafe.aircrafts.domain.AircraftStatus;
import static org.junit.jupiter.api.Assertions.*;

class UpdateAircraftStatusRequestTest {

    @Test void createsCorrectly() {
        UpdateAircraftStatusRequest req = new UpdateAircraftStatusRequest(AircraftStatus.IN_FLIGHT);
        assertEquals(AircraftStatus.IN_FLIGHT, req.status());
    }
}