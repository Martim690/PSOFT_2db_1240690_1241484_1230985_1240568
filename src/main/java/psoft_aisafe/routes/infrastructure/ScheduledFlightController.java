package psoft_aisafe.routes.infrastructure;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import psoft_aisafe.routes.application.CreateScheduledFlightUseCase;
import psoft_aisafe.routes.application.GetScheduledFlightsByAircraftUseCase;
import psoft_aisafe.routes.application.dtos.CreateScheduledFlightRequest;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled-flights")
public class ScheduledFlightController {

    private final CreateScheduledFlightUseCase createUseCase;
    private final GetScheduledFlightsByAircraftUseCase getByAircraftUseCase;

    public ScheduledFlightController(CreateScheduledFlightUseCase createUseCase,
                                     GetScheduledFlightsByAircraftUseCase getByAircraftUseCase) {
        this.createUseCase        = createUseCase;
        this.getByAircraftUseCase = getByAircraftUseCase;
    }

    /** US212 */
    @PostMapping
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<ScheduledFlightResponse> create(
            @Valid @RequestBody CreateScheduledFlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUseCase.execute(request));
    }

    /** US213 */
    @GetMapping("/aircraft/{registration}")
    @PreAuthorize("hasAuthority('ATCC')")
    public ResponseEntity<List<ScheduledFlightResponse>> getByAircraft(@PathVariable String registration) {
        return ResponseEntity.ok(getByAircraftUseCase.execute(registration));
    }
}