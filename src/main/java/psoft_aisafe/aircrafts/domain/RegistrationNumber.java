package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RegistrationNumber {

    // unique = true porque não podem existir 2 aviões com a mesma matrícula!
    @Column(name = "registration_number", unique = true, nullable = false)
    private String number;

    protected RegistrationNumber() {} // Exigido pelo JPA

    public RegistrationNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula não pode ser vazia.");
        }
        // Guardamos sempre em maiúsculas (ex: "cs-tpa" vira "CS-TPA")
        this.number = number.trim().toUpperCase();
    }

    public String getNumber() {
        return number;
    }
}