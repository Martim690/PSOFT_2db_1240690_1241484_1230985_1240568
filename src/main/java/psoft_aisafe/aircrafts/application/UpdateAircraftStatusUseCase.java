package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;

@Service
public class UpdateAircraftStatusUseCase {

    private final AircraftRepository aircraftRepository;

    public UpdateAircraftStatusUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Transactional
    public Aircraft execute(String registrationText, UpdateAircraftStatusRequest request) {
        RegistrationNumber registrationNumber = new RegistrationNumber(registrationText);

        Aircraft aircraft = aircraftRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with registration: " + registrationText));

        aircraft.updateStatus(request.status());

        return aircraftRepository.save(aircraft);
    }
}