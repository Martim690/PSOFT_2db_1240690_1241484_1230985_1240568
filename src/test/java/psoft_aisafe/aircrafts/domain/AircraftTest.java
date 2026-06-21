package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AircraftTest {

    private AircraftModel model;
    private RegistrationNumber reg;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        model = new AircraftModel("B737", 1000, 2000, 500, AircraftManufacturer.BOEING, "img.png");
        reg = new RegistrationNumber("CS-TPA");
        date = LocalDate.of(2020, 1, 1);
    }

    @Test void shouldCreateAircraftSuccessfully() { assertDoesNotThrow(() -> new Aircraft(reg, model, date, 150, AircraftStatus.AVAILABLE)); }
    @Test void registrationCannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new Aircraft(null, model, date, 150, AircraftStatus.AVAILABLE)); }
    @Test void modelCannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new Aircraft(reg, null, date, 150, AircraftStatus.AVAILABLE)); }
    @Test void manufactureDateCannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new Aircraft(reg, model, null, 150, AircraftStatus.AVAILABLE)); }
    @Test void manufactureDateCanBeInTheFuture() { assertDoesNotThrow(() -> new Aircraft(reg, model, LocalDate.now().plusDays(1), 150, AircraftStatus.AVAILABLE)); }
    @Test void manufactureDateCanBeToday() { assertDoesNotThrow(() -> new Aircraft(reg, model, LocalDate.now(), 150, AircraftStatus.AVAILABLE)); }
    @Test void passengerCapacityCannotBeNegative() { assertThrows(IllegalArgumentException.class, () -> new Aircraft(reg, model, date, -1, AircraftStatus.AVAILABLE)); }
    @Test void passengerCapacityCannotBeZero() { assertThrows(IllegalArgumentException.class, () -> new Aircraft(reg, model, date, 0, AircraftStatus.AVAILABLE)); }
    @Test void storesCurrentStatusCorrectly() { assertEquals(AircraftStatus.AVAILABLE, new Aircraft(reg, model, date, 150, AircraftStatus.AVAILABLE).getCurrentStatus()); }
    @Test void canCreateWithInFlightStatus() { assertEquals(AircraftStatus.IN_FLIGHT, new Aircraft(reg, model, date, 150, AircraftStatus.IN_FLIGHT).getCurrentStatus()); }
    @Test void canCreateWithMaintenanceStatus() { assertEquals(AircraftStatus.UNDER_MAINTENANCE, new Aircraft(reg, model, date, 150, AircraftStatus.UNDER_MAINTENANCE).getCurrentStatus()); }
    @Test void canCreateWithDeactivatedStatus() { assertEquals(AircraftStatus.INACTIVE, new Aircraft(reg, model, date, 150, AircraftStatus.INACTIVE).getCurrentStatus()); }
}