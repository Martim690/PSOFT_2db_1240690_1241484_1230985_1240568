package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
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
    public AircraftResponse execute(RegisterAircraftRequest request) {
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

        // Guarda na BD
        Aircraft savedAircraft = aircraftRepository.save(newAircraft);

        // Mapeia para o DTO de Saída
        return new AircraftResponse(
                savedAircraft.getRegistrationNumber().getNumber(),
                savedAircraft.getModel().getModelName(),
                savedAircraft.getManufacturingDate(),
                savedAircraft.getSeatingCapacity(),
                savedAircraft.getCurrentStatus().name()
        );
    }
}