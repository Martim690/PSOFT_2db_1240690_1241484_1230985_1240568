package psoft_aisafe.aircrafts.application.dtos;

import org.junit.jupiter.api.Test;
import psoft_aisafe.aircrafts.domain.AircraftStatus;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class RegisterAircraftRequestTest {

    @Test void createsCorrectly() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        RegisterAircraftRequest req = new RegisterAircraftRequest("CS-TPA", "B737", date, 150, AircraftStatus.AVAILABLE);
        assertEquals("CS-TPA", req.registrationNumber());
        assertEquals("B737", req.modelName());
        assertEquals(date, req.manufacturingDate());
        assertEquals(150, req.seatingCapacity());
        assertEquals(AircraftStatus.AVAILABLE, req.currentStatus());
    }
}