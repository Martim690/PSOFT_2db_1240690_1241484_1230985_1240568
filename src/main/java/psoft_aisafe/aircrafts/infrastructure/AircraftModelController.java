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

import psoft_aisafe.aircrafts.application.ListAircraftModelsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftModelUseCase;
import psoft_aisafe.aircrafts.application.UpdateAircraftModelSpecsUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftModelSpecsRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelController {

    private final RegisterAircraftModelUseCase registerAircraftModelUseCase;
    private final ListAircraftModelsUseCase listAircraftModelsUseCase;
    private final UpdateAircraftModelSpecsUseCase updateAircraftModelSpecsUseCase;

    public AircraftModelController(RegisterAircraftModelUseCase registerAircraftModelUseCase,  ListAircraftModelsUseCase listAircraftModelsUseCase, UpdateAircraftModelSpecsUseCase updateAircraftModelSpecsUseCase) {
        this.registerAircraftModelUseCase = registerAircraftModelUseCase;
        this.listAircraftModelsUseCase = listAircraftModelsUseCase;
        this.updateAircraftModelSpecsUseCase = updateAircraftModelSpecsUseCase;
    }

    @PostMapping
    @Operation(summary = "Register Aircraft Model (US101 / US202)")
    @ApiResponse(responseCode = "201", description = "CREATED")
    public ResponseEntity<EntityModel<AircraftModelResponse>> registerModel(@RequestBody @Valid RegisterAircraftModelRequest request) {
        AircraftModelResponse registeredModel = registerAircraftModelUseCase.execute(request);
        EntityModel<AircraftModelResponse> modelRepresentation = EntityModel.of(registeredModel,
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

    @PatchMapping("/{modelName}")
    @Operation(summary = "Update Aircraft Model Specifications (US201)")
    public ResponseEntity<EntityModel<AircraftModel>> updateSpecs(
            @PathVariable String modelName,
            @RequestBody @Valid UpdateAircraftModelSpecsRequest request) {

        AircraftModel updatedModel = updateAircraftModelSpecsUseCase.execute(modelName, request);

        EntityModel<AircraftModel> modelRepresentation = EntityModel.of(updatedModel,
                linkTo(methodOn(AircraftModelController.class).updateSpecs(modelName, null)).withSelfRel(),
                linkTo(methodOn(AircraftModelController.class).listModels()).withRel("all-models"));

        return ResponseEntity.ok(modelRepresentation);
    }
}