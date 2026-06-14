package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.application.dtos.TopUtilizedModelResponse;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetTopUtilizedModelsUseCase {

    private final AircraftRepository aircraftRepository;

    public GetTopUtilizedModelsUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<TopUtilizedModelResponse> execute(String sortBy) {
        List<Aircraft> allAircrafts = aircraftRepository.findAll();

        Map<AircraftModel, List<Aircraft>> groupedByModel = allAircrafts.stream()
                .collect(Collectors.groupingBy(Aircraft::getModel));

        List<TopUtilizedModelResponse> utilizedModels = groupedByModel.entrySet().stream()
                .map(entry -> {
                    AircraftModel model = entry.getKey();
                    List<Aircraft> aircrafts = entry.getValue();

                    int totalHours = aircrafts.stream().mapToInt(Aircraft::getTotalFlightHours).sum();
                    int totalAssignments = aircrafts.stream().mapToInt(Aircraft::getNumberOfAssignments).sum();

                    return new TopUtilizedModelResponse(
                            model.getModelName(),
                            model.getManufacturer().name(),
                            totalHours,
                            totalAssignments
                    );
                })
                .collect(Collectors.toList());

        if ("assignments".equalsIgnoreCase(sortBy)) {
            utilizedModels.sort((m1, m2) -> Integer.compare(m2.totalAssignments(), m1.totalAssignments()));
        } else {
            utilizedModels.sort((m1, m2) -> Integer.compare(m2.totalFlightHours(), m1.totalFlightHours()));
        }

        return utilizedModels.stream().limit(5).toList();
    }
}