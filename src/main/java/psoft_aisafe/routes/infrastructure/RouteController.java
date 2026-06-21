package psoft_aisafe.routes.infrastructure;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.routes.application.*;
import psoft_aisafe.routes.application.dtos.CreateRouteRequest;
import psoft_aisafe.routes.application.dtos.RouteResponse;
import psoft_aisafe.routes.application.dtos.RouteVersionResponse;
import psoft_aisafe.routes.application.dtos.UpdateRouteRequest;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import psoft_aisafe.routes.application.dtos.RouteWithStatsDTO;
import psoft_aisafe.routes.application.dtos.TotalNetworkDistanceResponse;
import psoft_aisafe.routes.application.dtos.AlternativeRoutesResponse;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final CreateRouteUseCase createRouteUseCase;
    private final UpdateRouteUseCase updateRouteUseCase;
    private final DeactivateRouteUseCase deactivateRouteUseCase;
    private final GetRouteByIdUseCase getRouteByIdUseCase;
    private final ListRoutesByAirportUseCase listRoutesByAirportUseCase;
    private final SearchRoutesUseCase searchRoutesUseCase;
    private final ListActiveRoutesSortedUseCase listActiveRoutesSortedUseCase;
    private final GetTotalNetworkDistanceUseCase getTotalNetworkDistanceUseCase;
    private final SearchAlternativeRoutesUseCase searchAlternativeRoutesUseCase;

    public RouteController(
            final CreateRouteUseCase createRouteUseCase,
            final UpdateRouteUseCase updateRouteUseCase,
            final DeactivateRouteUseCase deactivateRouteUseCase,
            final GetRouteByIdUseCase getRouteByIdUseCase,
            final ListRoutesByAirportUseCase listRoutesByAirportUseCase,
            final SearchRoutesUseCase searchRoutesUseCase, ListActiveRoutesSortedUseCase listActiveRoutesSortedUseCase, GetTotalNetworkDistanceUseCase getTotalNetworkDistanceUseCase, SearchAlternativeRoutesUseCase searchAlternativeRoutesUseCase) {
        this.createRouteUseCase = createRouteUseCase;
        this.updateRouteUseCase = updateRouteUseCase;
        this.deactivateRouteUseCase = deactivateRouteUseCase;
        this.getRouteByIdUseCase = getRouteByIdUseCase;
        this.listRoutesByAirportUseCase = listRoutesByAirportUseCase;
        this.searchRoutesUseCase = searchRoutesUseCase;
        this.listActiveRoutesSortedUseCase = listActiveRoutesSortedUseCase;
        this.getTotalNetworkDistanceUseCase = getTotalNetworkDistanceUseCase;
        this.searchAlternativeRoutesUseCase = searchAlternativeRoutesUseCase;
    }

    /**
     * US110 - Create a flight route.
     * POST /api/routes
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody CreateRouteRequest request) {
        RouteResponse response = createRouteUseCase.create(request);
        addLinks(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * US113 - Get route details by ID.
     * GET /api/routes/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ATCC', 'BACKOFFICE_OPERATOR')")
    public ResponseEntity<RouteResponse> getById(@PathVariable String id) {
        RouteResponse response = getRouteByIdUseCase.getById(id);
        addLinks(response);
        return ResponseEntity.ok(response);
    }

    /**
     * US111 - View route version history.
     * GET /api/routes/{id}/history
     */
    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyAuthority('ATCC', 'BACKOFFICE_OPERATOR')")
    public ResponseEntity<List<RouteVersionResponse>> getHistory(@PathVariable String id) {
        return ResponseEntity.ok(getRouteByIdUseCase.getHistory(id));
    }

    /**
     * US112 - Update a route (full replacement).
     * PUT /api/routes/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ATCC', 'BACKOFFICE_OPERATOR')")
    public ResponseEntity<RouteResponse> update(@PathVariable String id,
                                                @Valid @RequestBody UpdateRouteRequest request) {
        RouteResponse response = updateRouteUseCase.update(id, request);
        addLinks(response);
        return ResponseEntity.ok(response);
    }

    /**
     * US112 - Deactivate a route.
     * POST /api/routes/{id}/deactivate
     */
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ATCC', 'BACKOFFICE_OPERATOR')")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        deactivateRouteUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * US113 - List all routes from/to a specific airport.
     * GET /api/airports/{iata}/routes
     */
    @GetMapping("/airport/{iata}")
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<List<RouteResponse>> listByAirport(@PathVariable String iata) {
        List<RouteResponse> routes = listRoutesByAirportUseCase.listByAirport(iata);
        routes.forEach(this::addLinks);
        return ResponseEntity.ok(routes);
    }

    /**
     * US114 - Search routes by origin, destination, or both.
     * GET /api/routes?origin=LIS&destination=OPO
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<List<RouteResponse>> search(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {
        List<RouteResponse> routes = searchRoutesUseCase.search(origin, destination);
        routes.forEach(this::addLinks);
        return ResponseEntity.ok(routes);
    }

    /** US214 — Listar rotas ativas ordenadas por popularidade ou distância */
    @GetMapping("/active/sorted")
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<List<RouteWithStatsDTO>> listActiveRoutesSorted(
            @RequestParam(defaultValue = "popularity") String sortBy) {
        return ResponseEntity.ok(listActiveRoutesSortedUseCase.execute(sortBy));
    }

    /** US215 — Distância total da rede */
    @GetMapping("/total-distance")
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<TotalNetworkDistanceResponse> getTotalNetworkDistance() {
        return ResponseEntity.ok(getTotalNetworkDistanceUseCase.execute());
    }

    /** US216 — Rotas alternativas entre dois aeroportos */
    @GetMapping("/alternatives")
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<AlternativeRoutesResponse> getAlternativeRoutes(
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(searchAlternativeRoutesUseCase.execute(origin, destination));
    }

    // HATEOAS links
    private void addLinks(RouteResponse response) {
        response.add(linkTo(methodOn(RouteController.class).getById(response.getRouteId())).withSelfRel());
        response.add(linkTo(methodOn(RouteController.class).getHistory(response.getRouteId())).withRel("history"));
        response.add(linkTo(methodOn(RouteController.class).listByAirport(response.getOriginIataCode())).withRel("origin-routes"));
        response.add(linkTo(methodOn(RouteController.class).listByAirport(response.getDestinationIataCode())).withRel("destination-routes"));
    }
}