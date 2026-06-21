package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftOperationalHoursResponseTest {

    @Test void createsCorrectly() {
        AircraftOperationalHoursResponse res = new AircraftOperationalHoursResponse("CS-TPA", 1250);
        assertEquals("CS-TPA", res.registrationNumber());
        assertEquals(1250, res.totalFlightHours());
    }
}