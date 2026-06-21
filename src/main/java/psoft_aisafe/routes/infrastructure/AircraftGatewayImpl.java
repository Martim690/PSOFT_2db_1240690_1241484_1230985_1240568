// routes/infrastructure/AircraftGatewayImpl.java
package psoft_aisafe.routes.infrastructure;

import org.springframework.stereotype.Component;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.AircraftStatus;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;
import psoft_aisafe.routes.domain.AircraftGateway;

import java.util.Optional;

@Component
public class AircraftGatewayImpl implements AircraftGateway {

    private final AircraftRepository aircraftRepository;

    public AircraftGatewayImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Optional<AircraftInfo> findByRegistration(String registration) {
        return aircraftRepository
                .findByRegistrationNumber(new RegistrationNumber(registration))
                .map(a -> new AircraftInfo(
                        a.getRegistrationNumber().getNumber(),
                        a.getCurrentStatus() == AircraftStatus.UNDER_MAINTENANCE,
                        a.getModel().getMaximumRange(),
                        a.getSeatingCapacity()
                ));
    }
}