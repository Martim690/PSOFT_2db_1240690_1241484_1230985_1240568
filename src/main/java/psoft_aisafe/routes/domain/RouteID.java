package psoft_aisafe.routes.domain;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class RouteID {

    private String routeId;

    protected RouteID() {}

    private RouteID(final String routeId) {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        this.routeId = routeId;
    }

    public static RouteID generate() {
        return new RouteID(UUID.randomUUID().toString());
    }

    public static RouteID of(final String routeId) {
        return new RouteID(routeId);
    }

    public String getRouteId() {
        return routeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteID)) return false;
        RouteID other = (RouteID) o;
        return Objects.equals(routeId, other.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }

    @Override
    public String toString() {
        return routeId;
    }
}