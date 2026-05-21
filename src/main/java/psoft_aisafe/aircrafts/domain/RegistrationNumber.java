package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class RegistrationNumber {
    @Column(name = "registration_number", unique = true, nullable = false)
    private String number;

    protected RegistrationNumber() {}

    public RegistrationNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }
        this.number = number.trim().toUpperCase();
    }
}