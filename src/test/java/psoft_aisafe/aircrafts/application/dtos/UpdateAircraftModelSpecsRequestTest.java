package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateAircraftModelSpecsRequestTest {

    @Test void createsCorrectly() {
        UpdateAircraftModelSpecsRequest req = new UpdateAircraftModelSpecsRequest(100, 200, 300);
        assertEquals(100, req.fuelCapacity());
        assertEquals(200, req.maximumRange());
        assertEquals(300, req.cruisingSpeed());
    }
}