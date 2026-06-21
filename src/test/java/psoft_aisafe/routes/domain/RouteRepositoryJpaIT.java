package psoft_aisafe.routes.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import psoft_aisafe.routes.infrastructure.RouteRepositoryJpa;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RouteRepositoryJpaIT {

    @Autowired
    private RouteRepositoryJpa repository;

    private Route createAndSaveRoute(String origin, String dest, boolean active) {
        RouteRequirements requirements = new RouteRequirements(400, 50, "B737");
        Route route = new Route(origin, dest, requirements, 300.0, 50);
        if (!active) {
            route.deactivate();
        }
        return repository.save(route);
    }

    @Test
    void shouldSaveAndRetrieveRouteByRouteId() {
        // Arrange
        Route saved = createAndSaveRoute("LIS", "OPO", true);

        // Act
        Optional<Route> retrieved = repository.findByRouteId(saved.getRouteId());

        // Assert
        assertTrue(retrieved.isPresent());
        assertEquals(saved.getRouteId(), retrieved.get().getRouteId());
        assertEquals("LIS", retrieved.get().getOriginIataCode());
        assertEquals("OPO", retrieved.get().getDestinationIataCode());
    }

    @Test
    void shouldFindByOriginOrDestination() {
        // Arrange
        Route r1 = createAndSaveRoute("LIS", "OPO", true);
        Route r2 = createAndSaveRoute("OPO", "MAD", true);
        Route r3 = createAndSaveRoute("LIS", "MAD", true);

        // Act
        List<Route> routes = repository.findByOriginOrDestination("OPO");

        // Assert
        assertEquals(2, routes.size());
        assertTrue(routes.stream().anyMatch(r -> r.getRouteId().equals(r1.getRouteId())));
        assertTrue(routes.stream().anyMatch(r -> r.getRouteId().equals(r2.getRouteId())));
    }

    @Test
    void shouldSearchRoutesWithFilters() {
        // Arrange
        Route r1 = createAndSaveRoute("LIS", "OPO", true);
        Route r2 = createAndSaveRoute("LIS", "MAD", true);

        // Act & Assert
        // Search by both
        List<Route> search1 = repository.search("LIS", "OPO");
        assertEquals(1, search1.size());
        assertEquals(r1.getRouteId(), search1.get(0).getRouteId());

        // Search by origin only
        List<Route> search2 = repository.search("LIS", null);
        assertEquals(2, search2.size());

        // Search by destination only
        List<Route> search3 = repository.search(null, "MAD");
        assertEquals(1, search3.size());
        assertEquals(r2.getRouteId(), search3.get(0).getRouteId());
    }

    @Test
    void shouldCheckIfActiveRouteExistsBetweenAirports() {
        // Arrange
        Route r1 = createAndSaveRoute("LIS", "OPO", true);

        // Act & Assert
        assertTrue(repository.existsActiveRouteBetween("LIS", "OPO"));
        assertFalse(repository.existsActiveRouteBetween("LIS", "MAD"));

        // Deactivate route and assert it's no longer found as active
        r1.deactivate();
        repository.save(r1);
        assertFalse(repository.existsActiveRouteBetween("LIS", "OPO"));
    }

    @Test
    void shouldFindByActiveTrueOnly() {
        // Arrange
        Route active = createAndSaveRoute("LIS", "OPO", true);
        Route inactive = createAndSaveRoute("LIS", "MAD", false);

        // Act
        List<Route> activeRoutes = repository.findByActiveTrue();

        // Assert
        assertEquals(1, activeRoutes.size());
        assertEquals(active.getRouteId(), activeRoutes.get(0).getRouteId());
    }

    @Test
    void shouldFindByOriginAndActiveTrue() {
        // Arrange
        Route active1 = createAndSaveRoute("LIS", "OPO", true);
        Route active2 = createAndSaveRoute("MAD", "OPO", true);
        Route inactive = createAndSaveRoute("LIS", "MAD", false);

        // Act
        List<Route> routes = repository.findByOriginIataCodeAndActiveTrue("LIS");

        // Assert
        assertEquals(1, routes.size());
        assertEquals(active1.getRouteId(), routes.get(0).getRouteId());
    }

    @Test
    void shouldFindByDestinationAndActiveTrue() {
        // Arrange
        Route active1 = createAndSaveRoute("LIS", "OPO", true);
        Route active2 = createAndSaveRoute("MAD", "OPO", true);
        Route inactive = createAndSaveRoute("LIS", "MAD", false);

        // Act
        List<Route> routes = repository.findByDestinationIataCodeAndActiveTrue("OPO");

        // Assert
        assertEquals(2, routes.size());
        assertTrue(routes.stream().anyMatch(r -> r.getRouteId().equals(active1.getRouteId())));
        assertTrue(routes.stream().anyMatch(r -> r.getRouteId().equals(active2.getRouteId())));
    }

    @Test
    void shouldFindByOriginAndDestinationAndActiveTrue() {
        // Arrange
        Route active = createAndSaveRoute("LIS", "OPO", true);
        Route inactive = createAndSaveRoute("LIS", "OPO", false);

        // Act
        List<Route> routes = repository.findByOriginIataCodeAndDestinationIataCodeAndActiveTrue("LIS", "OPO");

        // Assert
        assertEquals(1, routes.size());
        assertEquals(active.getRouteId(), routes.get(0).getRouteId());
    }
}
