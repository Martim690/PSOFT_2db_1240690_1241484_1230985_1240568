package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FleetStatusResponseTest {

    @Test void createsCorrectly() {
        FleetStatusResponse res = new FleetStatusResponse(Map.of("AVAILABLE", 1L), Collections.emptyList());
        assertTrue(res.aircrafts().isEmpty());
        assertNotNull(res.statusCounts());
        assertEquals(1L, res.statusCounts().get("AVAILABLE"));
    }
}