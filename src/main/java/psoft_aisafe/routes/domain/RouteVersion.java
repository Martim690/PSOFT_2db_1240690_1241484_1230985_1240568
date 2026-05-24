package psoft_aisafe.routes.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_versions")
public class RouteVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private int estimatedFlightTime; // in minutes

    @Column(nullable = false)
    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private String changeReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "route_id", referencedColumnName = "routeId")
    })
    private Route route;

    protected RouteVersion() {}

    public RouteVersion(final double distance,
                        final int estimatedFlightTime,
                        final LocalDateTime validFrom,
                        final String changeReason,
                        final Route route) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }
        if (estimatedFlightTime <= 0) {
            throw new IllegalArgumentException("Estimated flight time must be positive");
        }
        if (validFrom == null) {
            throw new IllegalArgumentException("Valid from date cannot be null");
        }
        this.distance = distance;
        this.estimatedFlightTime = estimatedFlightTime;
        this.validFrom = validFrom;
        this.changeReason = changeReason;
        this.route = route;
    }

    public void closeVersion(final LocalDateTime closedAt) {
        this.validUntil = closedAt;
    }

    public boolean isCurrent() {
        return validUntil == null;
    }

    public Long getId() { return id; }
    public double getDistance() { return distance; }
    public int getEstimatedFlightTime() { return estimatedFlightTime; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public String getChangeReason() { return changeReason; }
    public Route getRoute() { return route; }
}