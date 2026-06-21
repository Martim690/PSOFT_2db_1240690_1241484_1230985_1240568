package psoft_aisafe.routes.domain;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    Route save(Route route);

    Optional<Route> findByRouteId(RouteID routeId);

    /** US113 - Find all routes from a specific airport (as origin or destination). */
    List<Route> findByOriginOrDestination(String iataCode);

    /** US114 - Search routes by origin, destination, or both. */
    List<Route> search(String origin, String destination);

    /** Check if a route already exists between two airports (active only). */
    boolean existsActiveRouteBetween(String origin, String destination);

    List<Route> findAll();

    /** US214 / US215 */
    List<Route> findByActiveTrue();

    /** US216 — primeira perna */
    List<Route> findByOriginIataCodeAndActiveTrue(String originIataCode);

    /** US216 — segunda perna */
    List<Route> findByDestinationIataCodeAndActiveTrue(String destinationIataCode);

    /** US216 — rotas diretas entre dois aeroportos */
    List<Route> findByOriginIataCodeAndDestinationIataCodeAndActiveTrue(
            String originIataCode, String destinationIataCode);
}