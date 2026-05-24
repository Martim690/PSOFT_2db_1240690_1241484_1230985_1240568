package psoft_aisafe.routes.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "routes")
public class Route {

    @EmbeddedId
    private RouteID routeId;

    @Column(name = "origin_iata_code", nullable = false)
    private String originIataCode;

    @Column(name = "destination_iata_code", nullable = false)
    private String destinationIataCode;

    @Embedded
    private RouteRequirements requirements;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("validFrom DESC")
    private List<RouteVersion> versions = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive;

    @Version
    private Long version;

    protected Route() {}

    public Route(final String originIataCode,
                 final String destinationIataCode,
                 final RouteRequirements requirements,
                 final double distance,
                 final int estimatedFlightTime) {
        if (originIataCode == null || originIataCode.isBlank()) {
            throw new IllegalArgumentException("Origin IATA code cannot be null");
        }
        if (destinationIataCode == null || destinationIataCode.isBlank()) {
            throw new IllegalArgumentException("Destination IATA code cannot be null");
        }
        if (originIataCode.equals(destinationIataCode)) {
            throw new IllegalArgumentException("Origin and destination cannot be the same airport");
        }
        if (requirements == null) {
            throw new IllegalArgumentException("Route requirements cannot be null");
        }

        this.routeId = RouteID.generate();
        this.originIataCode = originIataCode;
        this.destinationIataCode = destinationIataCode;
        this.requirements = requirements;
        this.isActive = true;

        RouteVersion firstVersion = new RouteVersion(distance, estimatedFlightTime, LocalDateTime.now(), "Initial creation", this);
        this.versions.add(firstVersion);
    }

    public void update(final double newDistance,
                       final int newEstimatedFlightTime,
                       final RouteRequirements newRequirements,
                       final String changeReason) {
        getCurrentVersion().ifPresent(v -> v.closeVersion(LocalDateTime.now()));
        RouteVersion newVersion = new RouteVersion(newDistance, newEstimatedFlightTime, LocalDateTime.now(), changeReason, this);
        this.versions.add(newVersion);
        if (newRequirements != null) {
            this.requirements = newRequirements;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public Optional<RouteVersion> getCurrentVersion() {
        return versions.stream().filter(RouteVersion::isCurrent).findFirst();
    }

    public RouteID getRouteId() { return routeId; }
    public String getOriginIataCode() { return originIataCode; }
    public String getDestinationIataCode() { return destinationIataCode; }
    public RouteRequirements getRequirements() { return requirements; }
    public List<RouteVersion> getVersions() { return Collections.unmodifiableList(versions); }
    public boolean isActive() { return isActive; }
}