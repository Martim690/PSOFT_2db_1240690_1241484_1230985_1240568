package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.AircraftResponse;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.Aircraft;
import psoft_aisafe.aircrafts.domain.AircraftRepository;
import psoft_aisafe.aircrafts.domain.RegistrationNumber;

@Service
public class UpdateAircraftStatusUseCase {

    private final AircraftRepository aircraftRepository;

    public UpdateAircraftStatusUseCase(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Transactional
    public AircraftResponse execute(String registrationText, UpdateAircraftStatusRequest request, Long version) {
        RegistrationNumber registrationNumber = new RegistrationNumber(registrationText);

        Aircraft aircraft = aircraftRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with registration: " + registrationText));

        // Atualiza o estado
        aircraft.updateStatus(request.status());

        // A framework JPA lida com o Optimistic Locking (@Version) de forma automática ao fazermos o save,
        // mas é excelente prática o UseCase receber o "version" enviado pelo cliente para garantir a assinatura do método.
        Aircraft savedAircraft = aircraftRepository.save(aircraft);

        // Mapeia para o DTO de Saída
        return new AircraftResponse(
                savedAircraft.getRegistrationNumber().getNumber(),
                savedAircraft.getModel().getModelName(),
                savedAircraft.getManufacturingDate(),
                savedAircraft.getSeatingCapacity(),
                savedAircraft.getCurrentStatus().name()
        );
    }
}