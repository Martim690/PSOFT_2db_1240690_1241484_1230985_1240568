package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AircraftTest {

    @Test
    void shouldCreateAircraftSuccessfully() {
        RegistrationNumber reg = new RegistrationNumber("CS-TPA");
        AircraftModel model = new AircraftModel("B737", 20000, 5000, 800, AircraftManufacturer.BOEING);

        Aircraft aircraft = new Aircraft(reg, model, LocalDate.now(), 180, AircraftStatus.AVAILABLE);

        assertNotNull(aircraft);
        assertEquals(180, aircraft.getSeatingCapacity());
        assertEquals(AircraftStatus.AVAILABLE, aircraft.getCurrentStatus());
    }

    @Test
    void shouldFailIfSeatingCapacityIsNegative() {
        RegistrationNumber reg = new RegistrationNumber("CS-TPA");
        AircraftModel model = new AircraftModel("B737", 20000, 5000, 800, AircraftManufacturer.BOEING);

        assertThrows(IllegalArgumentException.class, () -> {
            new Aircraft(reg, model, LocalDate.now(), -10, AircraftStatus.AVAILABLE);
        });
    }

    @Test
    void shouldFailIfStatusIsNull() {
        RegistrationNumber reg = new RegistrationNumber("CS-TPA");
        AircraftModel model = new AircraftModel("B737", 20000, 5000, 800, AircraftManufacturer.BOEING);

        assertThrows(IllegalArgumentException.class, () -> {
            new Aircraft(reg, model, LocalDate.now(), 180, null);
        });
    }
}