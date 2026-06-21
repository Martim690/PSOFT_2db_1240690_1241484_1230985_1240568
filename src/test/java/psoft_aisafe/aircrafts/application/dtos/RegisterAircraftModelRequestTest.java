package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import psoft_aisafe.aircrafts.domain.AircraftManufacturer;
import static org.junit.jupiter.api.Assertions.*;

class RegisterAircraftModelRequestTest {

    @Test void createsCorrectly() {
        RegisterAircraftModelRequest req = new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 1000, 2000, 500);
        assertEquals(AircraftManufacturer.BOEING, req.manufacturer());
        assertEquals("B737", req.modelName());
        assertEquals(1000, req.fuelCapacity());
        assertEquals(2000, req.maximumRange());
        assertEquals(500, req.cruisingSpeed());
    }
}