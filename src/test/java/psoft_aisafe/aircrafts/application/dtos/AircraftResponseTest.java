package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AircraftResponseTest {

    @Test void createsCorrectly() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        AircraftResponse res = new AircraftResponse("CS-TPA", "B737", date, 150, "AVAILABLE");
        assertEquals("CS-TPA", res.registrationNumber());
        assertEquals("B737", res.model());
        assertEquals(date, res.manufacturingDate());
        assertEquals(150, res.seatingCapacity());
    }
}