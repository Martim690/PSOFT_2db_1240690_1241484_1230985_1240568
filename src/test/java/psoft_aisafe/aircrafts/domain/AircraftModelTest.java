package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftModelTest {

    @Test void validCreation() { assertDoesNotThrow(() -> new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void validCreationWithEmptyUrl() { assertDoesNotThrow(() -> new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "")); }
    @Test void modelNameCannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel(null, 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void modelNameCannotBeEmpty() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void modelNameCannotBeBlank() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("   ", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void fuelCapacityCannotBeNegative() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", -1, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void fuelCapacityCannotBeZero() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 0, 2000, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void maximumRangeCannotBeNegative() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 1000, -1, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void maximumRangeCannotBeZero() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 1000, 0, 500, AircraftManufacturer.BOEING, "img.png")); }
    @Test void cruisingSpeedCannotBeNegative() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 1000, 2000, -1, AircraftManufacturer.BOEING, "img.png")); }
    @Test void cruisingSpeedCannotBeZero() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 1000, 2000, 0, AircraftManufacturer.BOEING, "img.png")); }
    @Test void manufacturerCannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new AircraftModel("B737", 1000, 2000, 500, null, "img.png")); }
    @Test void canCreateWithNullDiagramUrl() { assertDoesNotThrow(() -> new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, null)); }
    @Test void canCreateWithEmptyDiagramUrl() { assertDoesNotThrow(() -> new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "")); }
    @Test void canCreateWithBlankDiagramUrl() { assertDoesNotThrow(() -> new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "   ")); }

    @Test void updateSpecificationsSuccessfully() {
        AircraftModel model = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        model.updateSpecifications(1500, 2500, 600);
        assertEquals(1500, model.getFuelCapacity());
        assertEquals(2500, model.getMaximumRange());
        assertEquals(600, model.getCruisingSpeed());
    }

    @Test void updateSpecificationsWithNegativeFuel() {
        AircraftModel model = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        assertThrows(IllegalArgumentException.class, () -> model.updateSpecifications(-100, 2500, 600));
    }

    @Test void updateSpecificationsWithNegativeRange() {
        AircraftModel model = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        assertThrows(IllegalArgumentException.class, () -> model.updateSpecifications(1500, -2500, 600));
    }

    @Test void updateSpecificationsWithNegativeSpeed() {
        AircraftModel model = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        assertThrows(IllegalArgumentException.class, () -> model.updateSpecifications(1500, 2500, -600));
    }
}