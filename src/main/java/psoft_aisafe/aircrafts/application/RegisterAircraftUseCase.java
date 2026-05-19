package psoft_aisafe.aircrafts.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.domain.*;

@Service
public class RegisterAircraftUseCase {

    private final AircraftRepository aircraftRepository;
    private final AircraftModelRepository modelRepository;

    // Precisamos dos dois repositórios: um para procurar o modelo, outro para guardar o avião
    public RegisterAircraftUseCase(AircraftRepository aircraftRepository, AircraftModelRepository modelRepository) {
        this.aircraftRepository = aircraftRepository;
        this.modelRepository = modelRepository;
    }

    @Transactional
    public Aircraft execute(RegisterAircraftRequest request) {
        // 1. Criar o Value Object da Matrícula (ele faz as validações internas e converte para Maiúsculas)
        RegistrationNumber registration = new RegistrationNumber(request.registrationNumber());

        // 2. Verificar se já existe um avião com esta matrícula
        if (aircraftRepository.findByRegistrationNumber(registration).isPresent()) {
            throw new IllegalArgumentException("Aircraft with registration " + request.registrationNumber() + " already exists.");
        }

        // 3. Procurar o Modelo de Avião (ex: "B737-800")
        AircraftModel model = modelRepository.findByModelName(request.modelName())
                .orElseThrow(() -> new IllegalArgumentException("Aircraft model not found: " + request.modelName()));

        // 4. Criar a nova instância do Avião
        Aircraft newAircraft = new Aircraft(
                registration,
                model,
                request.manufacturingDate(),
                request.seatingCapacity(),
                request.currentStatus()
        );

        // 5. Guardar na base de dados
        return aircraftRepository.save(newAircraft);
    }
}
