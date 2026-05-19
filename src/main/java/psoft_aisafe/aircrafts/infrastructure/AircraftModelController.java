package psoft_aisafe.aircrafts.infrastructure;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<AircraftModel> registerModel(@RequestBody @Valid RegisterAircraftModelRequest request) {
        AircraftModel registeredModel = registerAircraftModelUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredModel);
    }

    @GetMapping
    public ResponseEntity<List<AircraftModel>> listModels() {
        List<AircraftModel> models = listAircraftModelsUseCase.execute();
        return ResponseEntity.ok(models);
    }
}
