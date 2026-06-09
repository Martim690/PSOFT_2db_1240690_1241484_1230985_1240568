package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.*;

@Service
public class RegisterAircraftUseCase {

    private final AircraftRepository aircraftRepository;
    private final AircraftModelRepository modelRepository;

    public RegisterAircraftUseCase(AircraftRepository aircraftRepository, AircraftModelRepository modelRepository) {
        this.aircraftRepository = aircraftRepository;
        this.modelRepository = modelRepository;
    }

    @Transactional
    public Aircraft execute(RegisterAircraftRequest request) {
        RegistrationNumber registration = new RegistrationNumber(request.registrationNumber());

        if (aircraftRepository.findByRegistrationNumber(registration).isPresent()) {
            throw new IllegalArgumentException("Aircraft with registration " + request.registrationNumber() + " already exists.");
        }

        AircraftModel model = modelRepository.findByModelName(request.modelName())
                .orElseThrow(() -> new IllegalArgumentException("Aircraft model not found: " + request.modelName()));

        Aircraft newAircraft = new Aircraft(
                registration,
                model,
                request.manufacturingDate(),
                request.seatingCapacity(),
                request.currentStatus()
        );

        return aircraftRepository.save(newAircraft);
    }
}