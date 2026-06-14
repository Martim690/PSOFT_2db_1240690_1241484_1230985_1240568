package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftModelSpecsRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

@Service
public class UpdateAircraftModelSpecsUseCase {

    private final AircraftModelRepository modelRepository;

    public UpdateAircraftModelSpecsUseCase(AircraftModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Transactional
    public AircraftModel execute(String modelName, UpdateAircraftModelSpecsRequest request) {
        AircraftModel model = modelRepository.findByModelName(modelName)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft model not found: " + modelName));

        int finalFuelCapacity = request.fuelCapacity() != null ? request.fuelCapacity() : model.getFuelCapacity();
        int finalMaximumRange = request.maximumRange() != null ? request.maximumRange() : model.getMaximumRange();
        int finalCruisingSpeed = request.cruisingSpeed() != null ? request.cruisingSpeed() : model.getCruisingSpeed();

        model.updateSpecifications(finalFuelCapacity, finalMaximumRange, finalCruisingSpeed);

        return modelRepository.save(model);
    }
}