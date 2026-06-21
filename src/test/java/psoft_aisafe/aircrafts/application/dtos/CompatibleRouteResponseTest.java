package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompatibleRouteResponseTest {

    @Test void createsCorrectly() {
        CompatibleRouteResponse res = new CompatibleRouteResponse("RT1", "OPO", "LIS", 100, 150, true);

        assertEquals("RT1", res.routeId());
        assertEquals("OPO", res.originIataCode());
        assertEquals("LIS", res.destinationIataCode());
        assertEquals(100, res.minimumRange());
        assertEquals(150, res.minimumCapacity());
        assertTrue(res.isActive());
    }
}