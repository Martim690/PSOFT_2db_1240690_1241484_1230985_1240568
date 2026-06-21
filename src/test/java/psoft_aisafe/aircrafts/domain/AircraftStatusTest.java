package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftStatusTest {

    @Test void containsAvailable() { assertEquals("AVAILABLE", AircraftStatus.AVAILABLE.name()); }
    @Test void containsInFlight() { assertEquals("IN_FLIGHT", AircraftStatus.IN_FLIGHT.name()); }
    @Test void containsUnderMaintenance() { assertEquals("UNDER_MAINTENANCE", AircraftStatus.UNDER_MAINTENANCE.name()); }
    @Test void containsDeactivated() { assertEquals("INACTIVE", AircraftStatus.INACTIVE.name()); }
    @Test void canParseAvailable() { assertEquals(AircraftStatus.AVAILABLE, AircraftStatus.valueOf("AVAILABLE")); }
    @Test void throwsOnInvalid() { assertThrows(IllegalArgumentException.class, () -> AircraftStatus.valueOf("INVALID")); }
}