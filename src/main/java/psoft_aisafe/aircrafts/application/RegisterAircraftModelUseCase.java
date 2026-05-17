package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

@Service
public class RegisterAircraftModelUseCase {

    private final AircraftModelRepository modelRepository;

    // Removemos o AircraftManufacturerRepository daqui!
    public RegisterAircraftModelUseCase(AircraftModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Transactional
    public AircraftModel execute(RegisterAircraftModelRequest request) {
        // 1. Check if the model already exists to prevent duplicates
        if (modelRepository.findByModelName(request.modelName()).isPresent()) {
            throw new IllegalArgumentException("Aircraft model already exists: " + request.modelName());
        }

        // 2. Register the Aircraft Model entity
        // Passamos o Enum 'request.manufacturerName()' diretamente!
        AircraftModel newModel = new AircraftModel(
                request.modelName(),
                request.fuelCapacity(),
                request.maximumRange(),
                request.cruisingSpeed(),
                request.manufacturer()
        );

        // 3. Save to the database and return
        return modelRepository.save(newModel);
    }
}