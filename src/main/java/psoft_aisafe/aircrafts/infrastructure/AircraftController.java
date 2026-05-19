package psoft_aisafe.aircrafts.infrastructure;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.aircrafts.application.GetAircraftByRegistrationUseCase;
import psoft_aisafe.aircrafts.application.ListAircraftsUseCase;
import psoft_aisafe.aircrafts.application.RegisterAircraftUseCase;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.Aircraft;

import java.util.List;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final RegisterAircraftUseCase registerAircraftUseCase;
    private final ListAircraftsUseCase listAircraftsUseCase;
    private final GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase;

    public AircraftController(RegisterAircraftUseCase registerAircraftUseCase, ListAircraftsUseCase listAircraftsUseCase, GetAircraftByRegistrationUseCase getAircraftByRegistrationUseCase) {
        this.registerAircraftUseCase = registerAircraftUseCase;
        this.listAircraftsUseCase = listAircraftsUseCase;
        this.getAircraftByRegistrationUseCase = getAircraftByRegistrationUseCase;
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

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<Aircraft> getAircraftByRegistration(@PathVariable String registrationNumber) {
        Aircraft aircraft = getAircraftByRegistrationUseCase.execute(registrationNumber);
        return ResponseEntity.ok(aircraft);
    }
}
