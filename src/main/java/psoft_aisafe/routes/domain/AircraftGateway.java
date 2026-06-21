package psoft_aisafe.routes.domain;

import java.util.Optional;

public interface AircraftGateway {

    record AircraftInfo(
            String registration,
            boolean underMaintenance,
            double maxRangeKm,
            int seatingCapacity
    ) {}

    Optional<AircraftInfo> findByRegistration(String registration);
}