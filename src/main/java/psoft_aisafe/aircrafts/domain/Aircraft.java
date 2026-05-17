package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Embutimos o Value Object
    @Embedded
    private RegistrationNumber registrationNumber;

    // Relação: Muitos aviões podem ser do mesmo modelo (Ex: TAP tem vários A320)
    @ManyToOne(optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private AircraftModel model;

    @Column(nullable = false)
    private LocalDate manufacturingDate;

    // Aqui está o Seating Capacity que decidimos colocar na instância física!
    @Column(nullable = false)
    private int seatingCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftStatus currentStatus;

    protected Aircraft() {}

    public Aircraft(RegistrationNumber registrationNumber, AircraftModel model, LocalDate manufacturingDate, int seatingCapacity, AircraftStatus currentStatus) {
        if (registrationNumber == null) throw new IllegalArgumentException("A matrícula é obrigatória.");
        if (model == null) throw new IllegalArgumentException("O modelo da aeronave é obrigatório.");
        if (manufacturingDate == null) throw new IllegalArgumentException("A data de fabrico é obrigatória.");
        if (seatingCapacity <= 0) throw new IllegalArgumentException("A capacidade de lugares deve ser positiva.");
        if (currentStatus == null) throw new IllegalArgumentException("O estado da aeronave é obrigatório.");

        this.registrationNumber = registrationNumber;
        this.model = model;
        this.manufacturingDate = manufacturingDate;
        this.seatingCapacity = seatingCapacity;
        this.currentStatus = currentStatus;
    }

    // Getters
    public Long getId() { return id; }
    public RegistrationNumber getRegistrationNumber() { return registrationNumber; }
    public AircraftModel getModel() { return model; }
    public LocalDate getManufacturingDate() { return manufacturingDate; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public AircraftStatus getCurrentStatus() { return currentStatus; }
}