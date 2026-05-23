package psoft_aisafe.aircrafts.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import psoft_aisafe.aircrafts.application.ListAircraftModelsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftModelUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelController {

    private final RegisterAircraftModelUseCase registerAircraftModelUseCase;
    private final ListAircraftModelsUseCase listAircraftModelsUseCase;

    public AircraftModelController(RegisterAircraftModelUseCase registerAircraftModelUseCase,  ListAircraftModelsUseCase listAircraftModelsUseCase) {
        this.registerAircraftModelUseCase = registerAircraftModelUseCase;
        this.listAircraftModelsUseCase = listAircraftModelsUseCase;
    }

    @PostMapping
    @Operation(summary = "Register Aircraft Model (US102)")
    public ResponseEntity<EntityModel<AircraftModel>> registerModel(@RequestBody @Valid RegisterAircraftModelRequest request) {
        AircraftModel registeredModel = registerAircraftModelUseCase.execute(request);
        EntityModel<AircraftModel> modelRepresentation = EntityModel.of(registeredModel,
                linkTo(methodOn(AircraftModelController.class).listModels()).withRel("all-models"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelRepresentation);
    }

    @GetMapping
    @Operation(summary = "List of Aircraft Models")
    public ResponseEntity<CollectionModel<EntityModel<AircraftModel>>> listModels() {
        List<AircraftModel> models = listAircraftModelsUseCase.execute();
        List<EntityModel<AircraftModel>> modelRepresentations = models.stream()
                .map(m -> EntityModel.of(m, linkTo(methodOn(AircraftModelController.class).listModels()).withSelfRel()))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(modelRepresentations,
                linkTo(methodOn(AircraftModelController.class).listModels()).withSelfRel()));
    }
}