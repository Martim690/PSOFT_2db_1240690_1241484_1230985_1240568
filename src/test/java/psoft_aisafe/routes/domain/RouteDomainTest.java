package psoft_aisafe.routes.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RouteDomainTest {

    @Test
    void shouldCreateRouteRequirementsSuccessfully() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");

        assertEquals(400, requirements.getMinimumRange());
        assertEquals(150, requirements.getMinimumCapacity());
        assertEquals("B737", requirements.getRequiredCertificationCode());
    }

    @Test
    void shouldThrowExceptionWhenMinimumRangeIsNegativeOrZero() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(0, 150, "B737");
        });
        assertEquals("Minimum range must be positive", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(-10, 150, "B737");
        });
        assertEquals("Minimum range must be positive", exception2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMinimumCapacityIsNegativeOrZero() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(400, 0, "B737");
        });
        assertEquals("Minimum capacity must be positive", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(400, -50, "B737");
        });
        assertEquals("Minimum capacity must be positive", exception2.getMessage());
    }

    @Test
    void shouldImplementEqualsAndHashCodeOnRouteRequirements() {
        RouteRequirements req1 = new RouteRequirements(400, 150, "B737");
        RouteRequirements req2 = new RouteRequirements(400, 150, "B737");
        RouteRequirements req3 = new RouteRequirements(500, 150, "B737");

        assertEquals(req1, req2);
        assertNotEquals(req1, req3);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotEquals(req1.hashCode(), req3.hashCode());
    }

    @Test
    void shouldCreateRouteSuccessfullyWithInitialVersion() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");
        Route route = new Route("LIS", "OPO", requirements, 300.0, 50);

        assertNotNull(route.getRouteId());
        assertNotNull(route.getRouteId().getRouteId());
        assertEquals("LIS", route.getOriginIataCode());
        assertEquals("OPO", route.getDestinationIataCode());
        assertEquals(requirements, route.getRequirements());
        assertTrue(route.isActive());
        assertEquals(1, route.getVersions().size());

        assertTrue(route.getCurrentVersion().isPresent());
        RouteVersion currentVersion = route.getCurrentVersion().get();
        assertEquals(300.0, currentVersion.getDistance());
        assertEquals(50, currentVersion.getEstimatedFlightTime());
        assertNull(currentVersion.getValidUntil());
        assertEquals("Initial creation", currentVersion.getChangeReason());
    }

    @Test
    void shouldThrowExceptionWhenOriginIataCodeIsInvalid() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new Route(null, "OPO", requirements, 300.0, 50);
        });
        assertEquals("Origin IATA code cannot be null", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new Route("", "OPO", requirements, 300.0, 50);
        });
        assertEquals("Origin IATA code cannot be null", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new Route("   ", "OPO", requirements, 300.0, 50);
        });
        assertEquals("Origin IATA code cannot be null", exception3.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDestinationIataCodeIsInvalid() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new Route("LIS", null, requirements, 300.0, 50);
        });
        assertEquals("Destination IATA code cannot be null", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new Route("LIS", "", requirements, 300.0, 50);
        });
        assertEquals("Destination IATA code cannot be null", exception2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOriginAndDestinationAreSame() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Route("LIS", "LIS", requirements, 300.0, 50);
        });
        assertEquals("Origin and destination cannot be the same airport", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequirementsAreNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Route("LIS", "OPO", null, 300.0, 50);
        });
        assertEquals("Route requirements cannot be null", exception.getMessage());
    }

    @Test
    void shouldCloseOldVersionAndOpenNewVersionOnUpdate() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");
        Route route = new Route("LIS", "OPO", requirements, 300.0, 50);
        RouteVersion initialVersion = route.getCurrentVersion().orElseThrow();

        RouteRequirements newRequirements = new RouteRequirements(500, 180, "A320");
        
        // Act
        route.update(310.0, 55, newRequirements, "Route optimized");

        // Assert
        assertFalse(initialVersion.isCurrent());
        assertNotNull(initialVersion.getValidUntil());

        assertTrue(route.getCurrentVersion().isPresent());
        RouteVersion currentVersion = route.getCurrentVersion().get();
        assertEquals(310.0, currentVersion.getDistance());
        assertEquals(55, currentVersion.getEstimatedFlightTime());
        assertNull(currentVersion.getValidUntil());
        assertEquals("Route optimized", currentVersion.getChangeReason());
        assertEquals(newRequirements, route.getRequirements());
        assertEquals(2, route.getVersions().size());
    }

    @Test
    void shouldDeactivateRouteSuccessfully() {
        RouteRequirements requirements = new RouteRequirements(400, 150, "B737");
        Route route = new Route("LIS", "OPO", requirements, 300.0, 50);

        assertTrue(route.isActive());
        route.deactivate();
        assertFalse(route.isActive());
    }
}
