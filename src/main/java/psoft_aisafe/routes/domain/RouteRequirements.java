package psoft_aisafe.routes.domain;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class RouteRequirements {

    private int minimumRange;
    private int minimumCapacity;
    private String requiredCertificationCode;

    protected RouteRequirements() {}

    public RouteRequirements(final int minimumRange,
                             final int minimumCapacity,
                             final String requiredCertificationCode) {
        if (minimumRange <= 0) {
            throw new IllegalArgumentException("Minimum range must be positive");
        }
        if (minimumCapacity <= 0) {
            throw new IllegalArgumentException("Minimum capacity must be positive");
        }
        this.minimumRange = minimumRange;
        this.minimumCapacity = minimumCapacity;
        this.requiredCertificationCode = requiredCertificationCode;
    }

    public int getMinimumRange() {
        return minimumRange;
    }

    public int getMinimumCapacity() {
        return minimumCapacity;
    }

    public String getRequiredCertificationCode() {
        return requiredCertificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteRequirements)) return false;
        RouteRequirements other = (RouteRequirements) o;
        return minimumRange == other.minimumRange
                && minimumCapacity == other.minimumCapacity
                && Objects.equals(requiredCertificationCode, other.requiredCertificationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumRange, minimumCapacity, requiredCertificationCode);
    }
}