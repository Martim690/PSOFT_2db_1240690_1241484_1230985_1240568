package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftModelResponseTest {

    @Test void createsCorrectly() {
        AircraftModelResponse res = new AircraftModelResponse("B737", "BOEING", 1000, 2000, 500, "url.png");
        assertEquals("B737", res.modelName());
        assertEquals("BOEING", res.manufacturer());
        assertEquals(1000, res.fuelCapacity());
        assertEquals(2000, res.maximumRange());
        assertEquals(500, res.cruisingSpeed());
    }
}