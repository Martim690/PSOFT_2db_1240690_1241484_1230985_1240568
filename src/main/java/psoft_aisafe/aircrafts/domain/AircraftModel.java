package psoft_aisafe.aircrafts.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "aircraft_models")
public class AircraftModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String modelName;

    @Column(nullable = false)
    private int fuelCapacity;
    private int maximumRange;
    private int cruisingSpeed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftManufacturer manufacturer;

    protected AircraftModel() {}

    public AircraftModel(String modelName, int fuelCapacity, int maximumRange, int cruisingSpeed, AircraftManufacturer manufacturer) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty.");
        }
        if (fuelCapacity <= 0) {
            throw new IllegalArgumentException("Fuel capacity must be greater than zero.");
        }
        if (maximumRange <= 0) {
            throw new IllegalArgumentException("Maximum range must be greater than zero.");
        }
        if (cruisingSpeed <= 0) {
            throw new IllegalArgumentException("Cruising speed must be greater than zero.");
        }
        if (manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer cannot be null.");
        }

        this.modelName = modelName;
        this.fuelCapacity = fuelCapacity;
        this.maximumRange = maximumRange;
        this.cruisingSpeed = cruisingSpeed;
        this.manufacturer = manufacturer;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public int getMaximumRange() {
        return maximumRange;
    }

    public int getCruisingSpeed() {
        return cruisingSpeed;
    }

    public AircraftManufacturer getManufacturer() {
        return manufacturer;
    }
}