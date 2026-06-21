package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TopUtilizedModelResponseTest {

    @Test void createsCorrectly() {
        TopUtilizedModelResponse res = new TopUtilizedModelResponse("B737", "BOEING", 5000, 10);

        assertEquals("B737", res.modelName());
        assertEquals("BOEING", res.manufacturer());
        assertEquals(5000, res.totalFlightHours());
        assertEquals(10, res.totalAssignments());
    }
}