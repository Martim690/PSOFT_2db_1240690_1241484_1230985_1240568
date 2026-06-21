package psoft_aisafe.routes.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_flights")
public class ScheduledFlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", nullable = false)
    private String routeId;

    @Column(name = "aircraft_registration", nullable = false)
    private String aircraftRegistration;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduledFlightStatus status;

    @Version
    private long version;

    protected ScheduledFlight() {}

    public ScheduledFlight(String routeId,
                           String aircraftRegistration,
                           LocalDateTime scheduledDateTime) {
        if (routeId == null || routeId.isBlank())
            throw new IllegalArgumentException("routeId é obrigatório");
        if (aircraftRegistration == null || aircraftRegistration.isBlank())
            throw new IllegalArgumentException("Matrícula da aeronave é obrigatória");
        if (scheduledDateTime == null)
            throw new IllegalArgumentException("Data/hora é obrigatória");
        if (scheduledDateTime.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Data/hora não pode ser no passado");

        this.routeId               = routeId;
        this.aircraftRegistration  = aircraftRegistration;
        this.scheduledDateTime     = scheduledDateTime;
        this.status                = ScheduledFlightStatus.SCHEDULED;
    }

    public Long getId()                        { return id; }
    public String getRouteId()                 { return routeId; }
    public String getAircraftRegistration()    { return aircraftRegistration; }
    public LocalDateTime getScheduledDateTime(){ return scheduledDateTime; }
    public ScheduledFlightStatus getStatus()   { return status; }

    public void cancel() {
        if (this.status != ScheduledFlightStatus.SCHEDULED)
            throw new IllegalStateException("Só é possível cancelar voos SCHEDULED");
        this.status = ScheduledFlightStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != ScheduledFlightStatus.SCHEDULED)
            throw new IllegalStateException("Só é possível completar voos SCHEDULED");
        this.status = ScheduledFlightStatus.COMPLETED;
    }
}