package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

@Service
public class RegisterAircraftModelUseCase {

    private final AircraftModelRepository modelRepository;

    public RegisterAircraftModelUseCase(AircraftModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Transactional
    public AircraftModel execute(RegisterAircraftModelRequest request) {
        if (modelRepository.findByModelName(request.modelName()).isPresent()) {
            throw new IllegalArgumentException("Aircraft model already exists: " + request.modelName());
        }

        AircraftModel newModel = new AircraftModel(
                request.modelName(),
                request.fuelCapacity(),
                request.maximumRange(),
                request.cruisingSpeed(),
                request.manufacturer()
        );

        return modelRepository.save(newModel);
    }
}