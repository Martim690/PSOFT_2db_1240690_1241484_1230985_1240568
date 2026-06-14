package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Version;

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

    @Column(nullable = false)
    private int totalFlightHours;

    @Column(nullable = false)
    private int numberOfAssignments;

    protected Aircraft() {}

    // Construtor original (mantido para os testes não falharem)
    public Aircraft(RegistrationNumber registrationNumber, AircraftModel model, LocalDate manufacturingDate, int seatingCapacity, AircraftStatus currentStatus) {
        this(registrationNumber, model, manufacturingDate, seatingCapacity, currentStatus, 0, 0);
    }

    // Novo construtor completo (Usado no Bootstrap)
    public Aircraft(RegistrationNumber registrationNumber, AircraftModel model, LocalDate manufacturingDate, int seatingCapacity, AircraftStatus currentStatus, int totalFlightHours, int numberOfAssignments) {
        if (registrationNumber == null) throw new IllegalArgumentException("Registration number is required.");
        if (model == null) throw new IllegalArgumentException("Aircraft model is required.");
        if (manufacturingDate == null) throw new IllegalArgumentException("Manufacturing date is required.");
        if (seatingCapacity <= 0) throw new IllegalArgumentException("Seating capacity must be positive.");
        if (currentStatus == null) throw new IllegalArgumentException("Aircraft status is required.");
        if (totalFlightHours < 0) throw new IllegalArgumentException("Total flight hours cannot be negative.");
        if (numberOfAssignments < 0) throw new IllegalArgumentException("Number of assignments cannot be negative.");

        this.registrationNumber = registrationNumber;
        this.model = model;
        this.manufacturingDate = manufacturingDate;
        this.seatingCapacity = seatingCapacity;
        this.currentStatus = currentStatus;
        this.totalFlightHours = totalFlightHours;
        this.numberOfAssignments = numberOfAssignments;
    }

    public void updateStatus(AircraftStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Aircraft status cannot be null.");
        }
        this.currentStatus = newStatus;
    }
}