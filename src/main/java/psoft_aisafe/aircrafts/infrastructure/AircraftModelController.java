package psoft_aisafe.aircrafts.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.aircrafts.application.ListAircraftModelsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftModelUseCase;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelController {

    private final RegisterAircraftModelUseCase registerAircraftModelUseCase;
    private final ListAircraftModelsUseCase listAircraftModelsUseCase;

    public AircraftModelController(RegisterAircraftModelUseCase registerAircraftModelUseCase,
                                   ListAircraftModelsUseCase listAircraftModelsUseCase) {
        this.registerAircraftModelUseCase = registerAircraftModelUseCase;
        this.listAircraftModelsUseCase = listAircraftModelsUseCase;
    }

    @PostMapping
    @Operation(summary = "Register Aircraft Model (US101)")
    public ResponseEntity<EntityModel<AircraftModelResponse>> registerModel(
            @RequestBody @Valid RegisterAircraftModelRequest request) {

        AircraftModelResponse response = registerAircraftModelUseCase.execute(request);

        EntityModel<AircraftModelResponse> modelRepresentation = EntityModel.of(response,
                linkTo(methodOn(AircraftModelController.class).listModels()).withRel("all-models"));

        return ResponseEntity.status(HttpStatus.CREATED).body(modelRepresentation);
    }

    @GetMapping
    @Operation(summary = "List of Aircraft Models")
    public ResponseEntity<CollectionModel<EntityModel<AircraftModelResponse>>> listModels() {
        List<AircraftModelResponse> models = listAircraftModelsUseCase.execute();

        List<EntityModel<AircraftModelResponse>> modelRepresentations = models.stream()
                .map(m -> EntityModel.of(m, linkTo(methodOn(AircraftModelController.class).listModels()).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(modelRepresentations,
                linkTo(methodOn(AircraftModelController.class).listModels()).withSelfRel()));
    }
}