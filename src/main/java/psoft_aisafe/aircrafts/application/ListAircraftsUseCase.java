package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;

import java.util.List;

@Service
public class ListAircraftsUseCase {

    private final AircraftRepository aircraftRepository;

    public ListAircraftsUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<Aircraft> execute() {
        // Devolve todas as instâncias físicas de aviões da base de dados
        return aircraftRepository.findAll();
    }
}