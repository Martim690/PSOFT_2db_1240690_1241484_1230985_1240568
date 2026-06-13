package psoft_aisafe.aircrafts.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import psoft_aisafe.aircrafts.application.GetAircraftByRegistrationUseCase;
import psoft_aisafe.aircrafts.application.ListAircraftsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftUseCase;
import psoft_aisafe.aircrafts.application.SearchAircraftsUseCase;
import psoft_aisafe.aircrafts.application.UpdateAircraftStatusUseCase;
import psoft_aisafe.aircrafts.application.GetCompatibleRoutesUseCase;
import psoft_aisafe.aircrafts.application.GetFleetStatusUseCase;
import psoft_aisafe.aircrafts.application.CalculateAircraftOperationalHoursUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
import psoft_aisafe.aircrafts.application.dtos.CompatibleRouteResponse;
import psoft_aisafe.aircrafts.application.dtos.FleetStatusResponse;
import psoft_aisafe.aircrafts.application.dtos.AircraftOperationalHoursResponse;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

import java.util.List;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final RegisterAircraftUseCase registerAircraftUseCase;
    private final ListAircraftsUseCase listAircraftsUseCase;
    private final SearchAircraftsUseCase searchAircraftsUseCase;
    private final GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase;
    private final UpdateAircraftStatusUseCase updateAircraftStatusUseCase;
    private final GetCompatibleRoutesUseCase getCompatibleRoutesUseCase;
    private final GetFleetStatusUseCase getFleetStatusUseCase;
    private final CalculateAircraftOperationalHoursUseCase calculateAircraftOperationalHoursUseCase;

    public AircraftController(RegisterAircraftUseCase registerAircraftUseCase, ListAircraftsUseCase listAircraftsUseCase, GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase, UpdateAircraftStatusUseCase updateAircraftStatusUseCase, SearchAircraftsUseCase searchAircraftsUseCase, GetCompatibleRoutesUseCase getCompatibleRoutesUseCase, GetFleetStatusUseCase getFleetStatusUseCase, CalculateAircraftOperationalHoursUseCase calculateAircraftOperationalHoursUseCase) {
        this.registerAircraftUseCase = registerAircraftUseCase;
        this.listAircraftsUseCase = listAircraftsUseCase;
        this.searchAircraftsUseCase = searchAircraftsUseCase;
        this.getAircraftByRegistrationUseCase = getAircraftByRegistrationUseCase;
        this.updateAircraftStatusUseCase = updateAircraftStatusUseCase;
        this.getCompatibleRoutesUseCase = getCompatibleRoutesUseCase;
        this.getFleetStatusUseCase = getFleetStatusUseCase;
        this.calculateAircraftOperationalHoursUseCase = calculateAircraftOperationalHoursUseCase;
    }

    @PostMapping
    @Operation(summary = "Register Aircraft (US102)")
    @ApiResponse(responseCode = "201", description = "CREATED")
    public ResponseEntity<EntityModel<Aircraft>> registerAircraft(@RequestBody @Valid RegisterAircraftRequest request) {
        Aircraft registeredAircraft = registerAircraftUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(registeredAircraft));
    }

    @GetMapping
    @Operation(summary = "List of Aircrafts")
    public ResponseEntity<CollectionModel<EntityModel<AircraftResponse>>> listAircrafts() {
        List<AircraftResponse> aircrafts = listAircraftsUseCase.execute();

        List<EntityModel<AircraftResponse>> aircraftModels = aircrafts.stream().map(this::toModel).toList();

        return ResponseEntity.ok(CollectionModel.of(aircraftModels,
                linkTo(methodOn(AircraftController.class).listAircrafts()).withSelfRel(),
                linkTo(methodOn(AircraftController.class).getAircrafts(null, null, null)).withRel("search-aircrafts")
        ));
    }

    @GetMapping("/search")
    @Operation(summary = "Search Aircraft by status, model or year (US104)")
    public ResponseEntity<CollectionModel<EntityModel<AircraftResponse>>> getAircrafts(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) AircraftStatus status,
            @RequestParam(required = false) Integer year) {

        List<AircraftResponse> aircrafts;

        if (model == null && status == null && year == null) {
            aircrafts = listAircraftsUseCase.execute();
        } else {
            aircrafts = searchAircraftsUseCase.execute(model, status, year);
        }

        List<EntityModel<AircraftResponse>> aircraftModels = aircrafts.stream().map(this::toModel).toList();

        return ResponseEntity.ok(CollectionModel.of(aircraftModels,
                linkTo(methodOn(AircraftController.class).getAircrafts(model, status, year)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts")
        ));
    }

    @GetMapping("/{registrationNumber}")
    @Operation(summary = "Search Aircraft by Registration Number (US103)")
    public ResponseEntity<EntityModel<AircraftResponse>> getAircraftByRegistration(@PathVariable String registrationNumber) {
        AircraftResponse aircraft = getAircraftByRegistrationUseCase.execute(registrationNumber);
        return ResponseEntity.ok(toModel(aircraft));
    }

    @GetMapping("/{registrationNumber}/compatible-routes")
    @Operation(summary = "View active routes compatible with a specific aircraft (US203)")
    public ResponseEntity<CollectionModel<EntityModel<CompatibleRouteResponse>>> getCompatibleRoutes(@PathVariable String registrationNumber) {

        List<CompatibleRouteResponse> compatibleRoutes = getCompatibleRoutesUseCase.execute(registrationNumber);

        List<EntityModel<CompatibleRouteResponse>> routeModels = compatibleRoutes.stream()
                .map(route -> EntityModel.of(route,
                        linkTo(methodOn(AircraftController.class).getCompatibleRoutes(registrationNumber)).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(routeModels,
                linkTo(methodOn(AircraftController.class).getCompatibleRoutes(registrationNumber)).withSelfRel()));
    }

    // Adaptar o toModel para o DTO
    private EntityModel<AircraftResponse> toModel(AircraftResponse aircraft) {
        String reg = aircraft.registrationNumber(); // Como é um Record, acede-se diretamente ao método do campo
        return EntityModel.of(aircraft,
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(reg)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).updateAircraftStatus(reg, null)).withRel("update-status"),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts")
        );
    }

    @GetMapping("/fleet-status")
    @Operation(summary = "Get Fleet Status Report Dashboard (US205)")
    public ResponseEntity<EntityModel<FleetStatusResponse>> getFleetStatus() {

        FleetStatusResponse report = getFleetStatusUseCase.execute();

        // Adiciona links HATEOAS para navegação
        EntityModel<FleetStatusResponse> modelRepresentation = EntityModel.of(report,
                linkTo(methodOn(AircraftController.class).getFleetStatus()).withSelfRel(),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts"));

        return ResponseEntity.ok(modelRepresentation);
    }

    @GetMapping("/operational-hours")
    @Operation(summary = "Calculate total operational hours for each aircraft (US206)")
    public ResponseEntity<CollectionModel<EntityModel<AircraftOperationalHoursResponse>>> getOperationalHours() {

        List<AircraftOperationalHoursResponse> hoursList = calculateAircraftOperationalHoursUseCase.execute();

        // Mapeia os links HATEOAS. O utilizador pode querer clicar no avião para ver mais detalhes.
        List<EntityModel<AircraftOperationalHoursResponse>> models = hoursList.stream()
                .map(hours -> EntityModel.of(hours,
                        linkTo(methodOn(AircraftController.class).getAircraftByRegistration(hours.registrationNumber())).withRel("aircraft-details")))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(AircraftController.class).getOperationalHours()).withSelfRel()));
    }

    @PatchMapping("/{registrationNumber}")
    @Operation(summary = "Update Aircraft status (US105)")
    public ResponseEntity<EntityModel<Aircraft>> updateAircraftStatus(
            @PathVariable String registrationNumber,
            @RequestBody @Valid UpdateAircraftStatusRequest request) {

        Aircraft updatedAircraft = updateAircraftStatusUseCase.execute(registrationNumber, request);

        String reg = updatedAircraft.getRegistrationNumber().getNumber();
        EntityModel<Aircraft> patchModel = EntityModel.of(updatedAircraft,
                linkTo(methodOn(AircraftController.class).updateAircraftStatus(reg, null)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(reg)).withRel("aircraft-details")
        );

        return ResponseEntity.ok(patchModel);
    }

    private EntityModel<Aircraft> toModel(Aircraft aircraft) {
        String reg = aircraft.getRegistrationNumber().getNumber();
        return EntityModel.of(aircraft,
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(reg)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).updateAircraftStatus(reg, null)).withRel("update-status"),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts")
        );
    }
}