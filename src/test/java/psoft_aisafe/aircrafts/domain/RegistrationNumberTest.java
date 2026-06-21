package psoft_aisafe.aircrafts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationNumberTest {

    @Test void validRegistrationCS() { assertDoesNotThrow(() -> new RegistrationNumber("CS-TPA")); }
    @Test void validRegistrationN() { assertDoesNotThrow(() -> new RegistrationNumber("N-12345")); }
    @Test void validRegistrationG() { assertDoesNotThrow(() -> new RegistrationNumber("G-BOAC")); }
    @Test void validRegistrationF() { assertDoesNotThrow(() -> new RegistrationNumber("F-WWBA")); }
    @Test void validRegistrationD() { assertDoesNotThrow(() -> new RegistrationNumber("D-ABYA")); }
    @Test void cannotBeNull() { assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber(null)); }
    @Test void cannotBeEmpty() { assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("")); }
    @Test void cannotBeBlankSpaces() { assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("   ")); }
    @Test void cannotBeLowercaseCS() { assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("cs-tpa")); }
    @Test void cannotBeLowercaseN() { assertThrows(IllegalArgumentException.class, () -> new RegistrationNumber("n-12345")); }
    @Test void valueIsStoredCorrectly() { assertEquals("CS-TPA", new RegistrationNumber("CS-TPA").getNumber()); }
    @Test void valueIsStoredCorrectlyForN() { assertEquals("N-12345", new RegistrationNumber("N-12345").getNumber()); }
    @Test void valueIsStoredCorrectlyForG() { assertEquals("G-BOAC", new RegistrationNumber("G-BOAC").getNumber()); }
    @Test void valueIsStoredCorrectlyForF() { assertEquals("F-WWBA", new RegistrationNumber("F-WWBA").getNumber()); }
    @Test void valueIsStoredCorrectlyForD() { assertEquals("D-ABYA", new RegistrationNumber("D-ABYA").getNumber()); }
}