package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

@Service
public class RegisterAircraftModelUseCase {

    private final AircraftModelRepository repository;

    public RegisterAircraftModelUseCase(AircraftModelRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AircraftModelResponse execute(RegisterAircraftModelRequest request) {
        // Cria a entidade
        AircraftModel model = new AircraftModel(
                request.modelName(),
                request.fuelCapacity(),
                request.maximumRange(),
                request.cruisingSpeed(),
                request.manufacturer()
        );

        // Guarda na BD
        AircraftModel savedModel = repository.save(model);

        // Mapeia para o DTO de Saída
        return new AircraftModelResponse(
                savedModel.getModelName(),
                savedModel.getManufacturer().name(),
                savedModel.getFuelCapacity(),
                savedModel.getMaximumRange(),
                savedModel.getCruisingSpeed()
        );
    }
}