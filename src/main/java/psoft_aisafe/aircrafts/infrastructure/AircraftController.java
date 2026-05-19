package psoft_aisafe.aircrafts.infrastructure;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.aircrafts.application.GetAircraftByRegistrationUseCase;
import psoft_aisafe.aircrafts.application.ListAircraftsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftUseCase;
import psoft_aisafe.aircrafts.application.SearchAircraftsUseCase;
import psoft_aisafe.aircrafts.application.UpdateAircraftStatusUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
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

    public AircraftController(RegisterAircraftUseCase registerAircraftUseCase, ListAircraftsUseCase listAircraftsUseCase, SearchAircraftsUseCase searchAircraftsUseCase, GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase, UpdateAircraftStatusUseCase updateAircraftStatusUseCase) {
        this.registerAircraftUseCase = registerAircraftUseCase;
        this.listAircraftsUseCase = listAircraftsUseCase;
        this.searchAircraftsUseCase = searchAircraftsUseCase;
        this.getAircraftByRegistrationUseCase = getAircraftByRegistrationUseCase;
        this.updateAircraftStatusUseCase = updateAircraftStatusUseCase;
    }

    @PostMapping
    public ResponseEntity<Aircraft> registerAircraft(@RequestBody @Valid RegisterAircraftRequest request) {
        Aircraft registeredAircraft = registerAircraftUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredAircraft);
    }

    @GetMapping
    public ResponseEntity<List<Aircraft>> listAircrafts() {
        List<Aircraft> aircrafts = listAircraftsUseCase.execute();
        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Aircraft>> getAircrafts(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) AircraftStatus status,
            @RequestParam(required = false) Integer year) {

        List<Aircraft> aircrafts;

        if (model == null && status == null && year == null) {
            aircrafts = listAircraftsUseCase.execute();
        }
        else {
            aircrafts = searchAircraftsUseCase.execute(model, status, year);
        }

        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<Aircraft> getAircraftByRegistration(@PathVariable String registrationNumber) {
        Aircraft aircraft = getAircraftByRegistrationUseCase.execute(registrationNumber);
        return ResponseEntity.ok(aircraft);
    }

    @PatchMapping("/{registrationNumber}/status")
    public ResponseEntity<Aircraft> updateAircraftStatus(
            @PathVariable String registrationNumber,
            @RequestBody @Valid UpdateAircraftStatusRequest request) {

        Aircraft updatedAircraft = updateAircraftStatusUseCase.execute(registrationNumber, request);
        return ResponseEntity.ok(updatedAircraft);
    }
}
