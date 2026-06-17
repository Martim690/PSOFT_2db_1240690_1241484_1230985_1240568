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
                    String diagram = model.getTechnicalDiagramUrl();

                    String finalUrl = (diagram == null || diagram.equals("empty.png") || diagram.equals("empty"))
                            ? ""
                            : "http://localhost:8080/diagrams/" + diagram;

                    return new AircraftModelResponse(
                            model.getModelName(),
                            model.getManufacturer().name(),
                            model.getFuelCapacity(),
                            model.getMaximumRange(),
                            model.getCruisingSpeed(),
                            finalUrl
                    );
                })
                .toList();
    }
}