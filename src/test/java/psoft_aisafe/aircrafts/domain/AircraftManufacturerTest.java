package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftManufacturerTest {

    @Test void containsBoeing() { assertEquals("BOEING", AircraftManufacturer.BOEING.name()); }
    @Test void containsAirbus() { assertEquals("AIRBUS", AircraftManufacturer.AIRBUS.name()); }
    @Test void canParseBoeing() { assertEquals(AircraftManufacturer.BOEING, AircraftManufacturer.valueOf("BOEING")); }
    @Test void throwsOnInvalid() { assertThrows(IllegalArgumentException.class, () -> AircraftManufacturer.valueOf("INVALID")); }
}