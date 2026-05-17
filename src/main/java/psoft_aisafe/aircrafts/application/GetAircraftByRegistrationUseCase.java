package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;

@Service
public class GetAircraftByRegistrationUseCase {

    private final AircraftRepository aircraftRepository;

    public GetAircraftByRegistrationUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public Aircraft execute(String registrationText) {
        // 1. Criar o Value Object (Isto garante logo que não é nulo/vazio e põe em Maiúsculas)
        RegistrationNumber registrationNumber = new RegistrationNumber(registrationText);

        // 2. Procurar na Base de Dados e devolver (ou dar erro se não existir)
        return aircraftRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with registration: " + registrationText));
    }
}