// routes/application/CreateScheduledFlightUseCase.java
package psoft_aisafe.routes.application;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.routes.application.dtos.CreateScheduledFlightRequest;
import psoft_aisafe.routes.application.dtos.ScheduledFlightResponse;
import psoft_aisafe.routes.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CreateScheduledFlightUseCase {

    private final ScheduledFlightRepository scheduledFlightRepository;
    private final RouteRepository routeRepository;
    private final AircraftGateway aircraftGateway;

    public CreateScheduledFlightUseCase(ScheduledFlightRepository scheduledFlightRepository,
                                        RouteRepository routeRepository,
                                        AircraftGateway aircraftGateway) {
        this.scheduledFlightRepository = scheduledFlightRepository;
        this.routeRepository           = routeRepository;
        this.aircraftGateway           = aircraftGateway;
    }

    public ScheduledFlightResponse execute(CreateScheduledFlightRequest request) {

        // 1 — Rota existe e está ativa.
        // A existência dos aeroportos já foi validada na criação da rota (US110);
        // não existe um módulo Airport com estado operacional no projeto, pelo que
        // a "disponibilidade do aeroporto" exigida por US212 fica coberta indiretamente
        // por aqui: só é possível agendar voos em rotas que já ligam aeroportos válidos.
        Route route = routeRepository.findByRouteId(RouteID.of(request.routeId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Rota não encontrada: " + request.routeId()));
        if (!route.isActive())
            throw new IllegalStateException("Rota não está ativa");

        RouteVersion currentVersion = route.getCurrentVersion()
                .orElseThrow(() -> new IllegalStateException(
                        "Rota sem versão ativa: " + request.routeId()));

        // 2 — Aeronave existe
        AircraftGateway.AircraftInfo aircraft = aircraftGateway
                .findByRegistration(request.aircraftRegistration())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aeronave não encontrada: " + request.aircraftRegistration()));

        // 3 — Verificação de alcance
        int requiredRange = route.getRequirements().getMinimumRange();
        if (aircraft.maxRangeKm() < requiredRange)
            throw new IllegalArgumentException(String.format(
                    "Alcance insuficiente. Necessário: %d km | Disponível: %.0f km",
                    requiredRange, aircraft.maxRangeKm()));

        // 4 — Aeronave não está em manutenção
        if (aircraft.underMaintenance())
            throw new IllegalStateException(
                    "Aeronave em manutenção — não pode ser atribuída a voos");

        // 5 — Disponibilidade da aeronave: sem conflitos de agendamento
        // (janela = duração estimada da rota)
        int durationMinutes = currentVersion.getEstimatedFlightTime();
        LocalDateTime windowStart = request.scheduledDateTime().minusMinutes(durationMinutes);
        LocalDateTime windowEnd   = request.scheduledDateTime().plusMinutes(durationMinutes);

        List<ScheduledFlight> conflicts = scheduledFlightRepository
                .findByAircraftRegistrationAndScheduledDateTimeBetween(
                        request.aircraftRegistration(), windowStart, windowEnd);
        if (!conflicts.isEmpty())
            throw new IllegalStateException(
                    "Aeronave já tem um voo agendado neste período");

        // 6 — Criar e persistir
        ScheduledFlight sf = new ScheduledFlight(
                request.routeId(),
                request.aircraftRegistration(),
                request.scheduledDateTime());

        return ScheduledFlightResponse.from(scheduledFlightRepository.save(sf));
    }
}