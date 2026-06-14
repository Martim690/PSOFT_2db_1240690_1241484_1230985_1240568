package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.AircraftModelResponse;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

import java.util.List;

@Service
public class ListAircraftModelsUseCase {

    private final AircraftModelRepository modelRepository;

    public ListAircraftModelsUseCase(AircraftModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<AircraftModelResponse> execute() {
        return modelRepository.findAll()
                .stream()
                .map(model -> {
                    // Validação para manter a consistência: se não houver diagrama, devolve "empty"
                    String diagram = model.getTechnicalDiagramUrl();
                    if (diagram == null || diagram.trim().isEmpty()) {
                        diagram = "empty";
                    }

                    return new AircraftModelResponse(
                            model.getModelName(),
                            model.getManufacturer().name(),
                            model.getFuelCapacity(),
                            model.getMaximumRange(),
                            model.getCruisingSpeed(),
                            diagram
                    );
                })
                .toList();
    }
}