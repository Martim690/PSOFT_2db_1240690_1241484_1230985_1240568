package psoft_aisafe.routes.domain;

import psoft_aisafe.model.classes.IATAcode;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    Route save(Route route);

    Optional<Route> findByRouteId(RouteID routeId);

    /**
     * US113 - Find all routes from a specific airport (as origin or destination).
     */
    List<Route> findByOriginOrDestination(String iataCode);

    /**
     * US114 - Search routes by origin, destination, or both.
     */
    List<Route> search(String origin, String destination);

    /**
     * Check if a route already exists between two airports (active only).
     */
    boolean existsActiveRouteBetween(String origin, String destination);
}