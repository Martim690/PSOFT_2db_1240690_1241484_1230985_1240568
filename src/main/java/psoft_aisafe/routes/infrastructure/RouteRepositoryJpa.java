package psoft_aisafe.routes.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteID;
import psoft_aisafe.routes.domain.RouteRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepositoryJpa extends JpaRepository<Route, RouteID>, RouteRepository {

    @Override
    Route save(Route route);

    @Override
    Optional<Route> findByRouteId(RouteID routeId);

    @Override
    @Query("SELECT r FROM Route r WHERE r.originIataCode = :iata OR r.destinationIataCode = :iata")
    List<Route> findByOriginOrDestination(@Param("iata") String iataCode);

    @Override
    @Query("""
        SELECT r FROM Route r
        WHERE (:origin IS NULL OR r.originIataCode = :origin)
          AND (:destination IS NULL OR r.destinationIataCode = :destination)
        """)
    List<Route> search(@Param("origin") String origin,
                       @Param("destination") String destination);

    @Override
    @Query("""
        SELECT COUNT(r) > 0 FROM Route r
        WHERE r.originIataCode = :origin
          AND r.destinationIataCode = :destination
          AND r.isActive = true
        """)
    boolean existsActiveRouteBetween(@Param("origin") String origin,
                                     @Param("destination") String destination);

    @Override
    @Query("SELECT r FROM Route r WHERE r.isActive = true")
    List<Route> findByActiveTrue();

    @Override
    @Query("SELECT r FROM Route r WHERE r.originIataCode = :originIataCode AND r.isActive = true")
    List<Route> findByOriginIataCodeAndActiveTrue(@Param("originIataCode") String originIataCode);

    @Override
    @Query("SELECT r FROM Route r WHERE r.destinationIataCode = :destinationIataCode AND r.isActive = true")
    List<Route> findByDestinationIataCodeAndActiveTrue(@Param("destinationIataCode") String destinationIataCode);

    @Override
    @Query("""
        SELECT r FROM Route r
        WHERE r.originIataCode = :originIataCode
          AND r.destinationIataCode = :destinationIataCode
          AND r.isActive = true
        """)
    List<Route> findByOriginIataCodeAndDestinationIataCodeAndActiveTrue(
            @Param("originIataCode") String originIataCode,
            @Param("destinationIataCode") String destinationIataCode);

    default Optional<Route> findByRouteId(String routeId) {
        return findByRouteId(RouteID.of(routeId));
    }
}