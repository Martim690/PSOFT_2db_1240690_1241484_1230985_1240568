package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationNumberTest {

    @Test
    void shouldCreateRegistrationNumberSuccessfully() {
        RegistrationNumber reg = new RegistrationNumber("CS-TPA");
        assertEquals("CS-TPA", reg.getNumber());
    }

    @Test
    void shouldFailIfRegistrationNumberIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RegistrationNumber(null);
        });
        assertEquals("Registration number cannot be null or blank.", exception.getMessage());
    }

    @Test
    void shouldFailIfRegistrationNumberIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("   "));
    }

    @Test
    void shouldFailIfRegistrationFormatIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("CS@TPA!"));
    }
}