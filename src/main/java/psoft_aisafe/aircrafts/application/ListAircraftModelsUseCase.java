package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.domain.AircraftModel;
import psoft_aisafe.aircrafts.domain.AircraftModelRepository;

import java.util.List;

@Service
public class ListAircraftModelsUseCase {

    private final AircraftModelRepository modelRepository;

    public ListAircraftModelsUseCase(AircraftModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<AircraftModel> execute() {
        return modelRepository.findAll();
    }
}