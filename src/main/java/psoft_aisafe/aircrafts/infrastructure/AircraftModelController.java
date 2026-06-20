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
import psoft_aisafe.aircrafts.application.GetTopUtilizedModelsUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftModelSpecsRequest;
import psoft_aisafe.aircrafts.application.dtos.TopUtilizedModelResponse;
import psoft_aisafe.aircrafts.domain.AircraftModel;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelController {

    private final RegisterAircraftModelUseCase registerAircraftModelUseCase;
    private final ListAircraftModelsUseCase listAircraftModelsUseCase;
    private final UpdateAircraftModelSpecsUseCase updateAircraftModelSpecsUseCase;
    private final GetTopUtilizedModelsUseCase getTopUtilizedModelsUseCase;

    public AircraftModelController(RegisterAircraftModelUseCase registerAircraftModelUseCase, ListAircraftModelsUseCase listAircraftModelsUseCase, UpdateAircraftModelSpecsUseCase updateAircraftModelSpecsUseCase, GetTopUtilizedModelsUseCase getTopUtilizedModelsUseCase) {
        this.registerAircraftModelUseCase = registerAircraftModelUseCase;
        this.listAircraftModelsUseCase = listAircraftModelsUseCase;
        this.updateAircraftModelSpecsUseCase = updateAircraftModelSpecsUseCase;
        this.getTopUtilizedModelsUseCase = getTopUtilizedModelsUseCase;
    }

    @org.springframework.web.bind.annotation.PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @io.swagger.v3.oas.annotations.Operation(summary = "Register Aircraft Model with an image file(US101 / US202)")
    public org.springframework.http.ResponseEntity<org.springframework.hateoas.EntityModel<psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse>> registerAircraftModel(
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.ModelAttribute psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest request,
            @org.springframework.web.bind.annotation.RequestParam(value = "file", required = false) org.springframework.web.multipart.MultipartFile file) {

        psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse savedModel = registerAircraftModelUseCase.execute(request, file);

        org.springframework.hateoas.EntityModel<psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse> resource = org.springframework.hateoas.EntityModel.of(savedModel);

        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(resource);
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
    public ResponseEntity<EntityModel<AircraftModelResponse>> updateSpecs(
            @PathVariable String modelName,
            @RequestBody @Valid UpdateAircraftModelSpecsRequest request) {

        AircraftModelResponse updatedModel = updateAircraftModelSpecsUseCase.execute(modelName, request);

        EntityModel<AircraftModelResponse> modelRepresentation = EntityModel.of(updatedModel,
                linkTo(methodOn(AircraftModelController.class).updateSpecs(modelName, null)).withSelfRel(),
                linkTo(methodOn(AircraftModelController.class).listModels()).withRel("all-models"));

        return ResponseEntity.ok(modelRepresentation);
    }

    @GetMapping("/top-utilized")
    @Operation(summary = "View Top 5 most utilized aircraft models (US204)")
    public ResponseEntity<CollectionModel<EntityModel<TopUtilizedModelResponse>>> getTopUtilizedModels(
            @RequestParam(required = false, defaultValue = "hours") String sortBy) {

        List<TopUtilizedModelResponse> topModels = getTopUtilizedModelsUseCase.execute(sortBy);

        List<EntityModel<TopUtilizedModelResponse>> modelRepresentations = topModels.stream()
                .map(m -> EntityModel.of(m, linkTo(methodOn(AircraftModelController.class).getTopUtilizedModels(sortBy)).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(modelRepresentations,
                linkTo(methodOn(AircraftModelController.class).getTopUtilizedModels(sortBy)).withSelfRel()));
    }
}