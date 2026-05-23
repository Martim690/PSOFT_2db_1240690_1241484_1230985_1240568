package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "aircrafts")
@Getter
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Embedded
    private RegistrationNumber registrationNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private AircraftModel model;

    @Column(nullable = false)
    private LocalDate manufacturingDate;

    @Column(nullable = false)
    private int seatingCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private AircraftStatus currentStatus;

    protected Aircraft() {}

    public Aircraft(RegistrationNumber registrationNumber, AircraftModel model, LocalDate manufacturingDate, int seatingCapacity, AircraftStatus currentStatus) {
        if (registrationNumber == null) throw new IllegalArgumentException("Registration number is required.");
        if (model == null) throw new IllegalArgumentException("Aircraft model is required.");
        if (manufacturingDate == null) throw new IllegalArgumentException("Manufacturing date is required.");
        if (seatingCapacity <= 0) throw new IllegalArgumentException("Seating capacity must be positive.");
        if (currentStatus == null) throw new IllegalArgumentException("Aircraft status is required.");

        this.registrationNumber = registrationNumber;
        this.model = model;
        this.manufacturingDate = manufacturingDate;
        this.seatingCapacity = seatingCapacity;
        this.currentStatus = currentStatus;
    }

    public void updateStatus(AircraftStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Aircraft status cannot be null.");
        }
        this.currentStatus = newStatus;
    }
}