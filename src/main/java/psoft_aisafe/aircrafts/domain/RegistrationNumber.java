package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class RegistrationNumber {
    @Column(name = "registration_number", unique = true, nullable = false)
    private String number;

    protected RegistrationNumber() {
    }

    public RegistrationNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be null or blank."); // Matched to test
        }
        if (!number.matches("^[A-Z0-9-]{3,10}$")) {
            throw new IllegalArgumentException("Invalid registration number format."); // Added regex validation
        }
        this.number = number;
    }
}
