package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftModelTest {

    @Test
    void shouldCreateAircraftModelSuccessfully() {
        AircraftModel model = new AircraftModel("B737", 20000, 5000, 800, AircraftManufacturer.BOEING);

        assertNotNull(model);
        assertEquals("B737", model.getModelName());
    }

    @Test
    void shouldFailIfModelNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftModel("", 20000, 5000, 800, AircraftManufacturer.BOEING);
        });
    }

    @Test
    void shouldFailIfFuelCapacityIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftModel("B737", -100, 5000, 800, AircraftManufacturer.BOEING);
        });
    }

    @Test
    void shouldFailIfManufacturerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftModel("B737", 20000, 5000, 800, null);
        });
    }
}