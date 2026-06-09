package psoft_aisafe.aircrafts.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.aircrafts.application.*;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.AircraftStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final RegisterAircraftUseCase registerAircraftUseCase;
    private final GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase;
    private final ListAircraftsUseCase listAircraftsUseCase;
    private final SearchAircraftsUseCase searchAircraftsUseCase;
    private final UpdateAircraftStatusUseCase updateAircraftStatusUseCase;

    public AircraftController(RegisterAircraftUseCase registerAircraftUseCase,
                              GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase,
                              ListAircraftsUseCase listAircraftsUseCase,
                              SearchAircraftsUseCase searchAircraftsUseCase,
                              UpdateAircraftStatusUseCase updateAircraftStatusUseCase) {
        this.registerAircraftUseCase = registerAircraftUseCase;
        this.getAircraftByRegistrationUseCase = getAircraftByRegistrationUseCase;
        this.listAircraftsUseCase = listAircraftsUseCase;
        this.searchAircraftsUseCase = searchAircraftsUseCase;
        this.updateAircraftStatusUseCase = updateAircraftStatusUseCase;
    }

    @PostMapping
    @Operation(summary = "Register Aircraft Instance (US102)")
    public ResponseEntity<EntityModel<AircraftResponse>> registerAircraft(
            @Valid @RequestBody RegisterAircraftRequest request) {

        AircraftResponse response = registerAircraftUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
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

        List<AircraftResponse> aircrafts = searchAircraftsUseCase.execute(model, status, year);
        List<EntityModel<AircraftResponse>> aircraftModels = aircrafts.stream().map(this::toModel).toList();

        return ResponseEntity.ok(CollectionModel.of(aircraftModels,
                linkTo(methodOn(AircraftController.class).getAircrafts(model, status, year)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts")
        ));
    }

    @GetMapping("/{registration}")
    @Operation(summary = "Get Aircraft Details (US103)")
    public ResponseEntity<EntityModel<AircraftResponse>> getAircraftByRegistration(@PathVariable String registration) {
        AircraftResponse response = getAircraftByRegistrationUseCase.execute(registration);
        return ResponseEntity.ok(toModel(response));
    }

    @PatchMapping("/{registration}/status")
    @Operation(summary = "Update Aircraft Status (US105)")
    public ResponseEntity<EntityModel<AircraftResponse>> updateStatus(
            @PathVariable String registration,
            @RequestHeader("If-Match") Long version,
            @Valid @RequestBody UpdateAircraftStatusRequest request) {

        AircraftResponse response = updateAircraftStatusUseCase.execute(registration, request, version);

        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(registration)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(registration)).withRel("aircraft-details")
        ));
    }

    // Helper para evitar duplicação de HATEOAS
    private EntityModel<AircraftResponse> toModel(AircraftResponse aircraft) {
        String reg = aircraft.registrationNumber();
        return EntityModel.of(aircraft,
                linkTo(methodOn(AircraftController.class).getAircraftByRegistration(reg)).withSelfRel(),
                linkTo(methodOn(AircraftController.class).updateStatus(reg, null, null)).withRel("update-status"),
                linkTo(methodOn(AircraftController.class).listAircrafts()).withRel("all-aircrafts")
        );
    }
}