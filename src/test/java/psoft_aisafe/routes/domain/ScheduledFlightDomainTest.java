package psoft_aisafe.routes.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledFlightDomainTest {

    @Test
    void shouldCreateScheduledFlightSuccessfully() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        ScheduledFlight sf = new ScheduledFlight("route-123", "CS-REG1", tomorrow);

        assertNotNull(sf);
        assertEquals("route-123", sf.getRouteId());
        assertEquals("CS-REG1", sf.getAircraftRegistration());
        assertEquals(tomorrow, sf.getScheduledDateTime());
        assertEquals(ScheduledFlightStatus.SCHEDULED, sf.getStatus());
        assertNull(sf.getId());
    }

    @Test
    void shouldThrowExceptionWhenRouteIdIsInvalid() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight(null, "CS-REG1", tomorrow);
        });
        assertEquals("routeId é obrigatório", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight("", "CS-REG1", tomorrow);
        });
        assertEquals("routeId é obrigatório", ex2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAircraftRegistrationIsInvalid() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight("route-123", null, tomorrow);
        });
        assertEquals("Matrícula da aeronave é obrigatória", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight("route-123", "   ", tomorrow);
        });
        assertEquals("Matrícula da aeronave é obrigatória", ex2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenScheduledDateTimeIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight("route-123", "CS-REG1", null);
        });
        assertEquals("Data/hora é obrigatória", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenScheduledDateTimeIsInPast() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight("route-123", "CS-REG1", yesterday);
        });
        assertEquals("Data/hora não pode ser no passado", ex.getMessage());
    }

    @Test
    void shouldCancelScheduledFlightSuccessfully() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        ScheduledFlight sf = new ScheduledFlight("route-123", "CS-REG1", tomorrow);

        assertEquals(ScheduledFlightStatus.SCHEDULED, sf.getStatus());
        sf.cancel();
        assertEquals(ScheduledFlightStatus.CANCELLED, sf.getStatus());
    }

    @Test
    void shouldCompleteScheduledFlightSuccessfully() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        ScheduledFlight sf = new ScheduledFlight("route-123", "CS-REG1", tomorrow);

        assertEquals(ScheduledFlightStatus.SCHEDULED, sf.getStatus());
        sf.complete();
        assertEquals(ScheduledFlightStatus.COMPLETED, sf.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonScheduledFlight() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        // Case 1: Already cancelled
        ScheduledFlight sf1 = new ScheduledFlight("route-123", "CS-REG1", tomorrow);
        sf1.cancel();
        IllegalStateException ex1 = assertThrows(IllegalStateException.class, sf1::cancel);
        assertEquals("Só é possível cancelar voos SCHEDULED", ex1.getMessage());

        // Case 2: Already completed
        ScheduledFlight sf2 = new ScheduledFlight("route-123", "CS-REG1", tomorrow);
        sf2.complete();
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, sf2::cancel);
        assertEquals("Só é possível cancelar voos SCHEDULED", ex2.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCompletingNonScheduledFlight() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        // Case 1: Already cancelled
        ScheduledFlight sf1 = new ScheduledFlight("route-123", "CS-REG1", tomorrow);
        sf1.cancel();
        IllegalStateException ex1 = assertThrows(IllegalStateException.class, sf1::complete);
        assertEquals("Só é possível completar voos SCHEDULED", ex1.getMessage());

        // Case 2: Already completed
        ScheduledFlight sf2 = new ScheduledFlight("route-123", "CS-REG1", tomorrow);
        sf2.complete();
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, sf2::complete);
        assertEquals("Só é possível completar voos SCHEDULED", ex2.getMessage());
    }

    /*
     * Nota de Arquitetura de Validação de Domínio:
     *
     * 1. A validação "falhar se a rota associada estiver inativa":
     *    A entidade ScheduledFlight apenas conhece o "routeId" como String, não possuindo uma
     *    dependência direta com o agregado Route para manter a separação conceitual e a
     *    consistência. Essa verificação é efetuada na camada de aplicação pelo caso de uso
     *    (CreateScheduledFlightUseCase) ao carregar a rota pelo repositório.
     *
     * 2. A validação "falhar se a data de chegada for anterior à partida":
     *    A entidade ScheduledFlight não possui atributo ou informação de "data de chegada",
     *    apenas guardando a data/hora de partida (scheduledDateTime). O tempo de voo estimado da rota
     *    é validado na entidade RouteVersion (onde é garantido que a duração é estritamente positiva,
     *    impedindo logicamente que o tempo de chegada ocorra antes ou no mesmo instante da partida).
     */
}
