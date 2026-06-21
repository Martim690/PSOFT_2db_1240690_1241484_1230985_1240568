package psoft_aisafe.routes.application.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateScheduledFlightRequest(

        @NotBlank(message = "O ID da rota é obrigatório")
        String routeId,

        @NotBlank(message = "A matrícula da aeronave é obrigatória")
        String aircraftRegistration,

        @NotNull(message = "A data/hora é obrigatória")
        @Future(message = "A data/hora deve ser no futuro")
        LocalDateTime scheduledDateTime
) {}