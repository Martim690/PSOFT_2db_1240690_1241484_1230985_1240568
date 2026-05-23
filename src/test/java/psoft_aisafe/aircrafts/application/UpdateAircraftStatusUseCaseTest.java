package psoft_aisafe.aircrafts.application;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftModelRequest;
import psoft_aisafe.aircrafts.application.dtos.RegisterAircraftRequest;
import psoft_aisafe.aircrafts.application.dtos.UpdateAircraftStatusRequest;
import psoft_aisafe.aircrafts.domain.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootTest
@Transactional
class UpdateAircraftStatusUseCaseTest {

    @Autowired private UpdateAircraftStatusUseCase updateUseCase;
    @Autowired private RegisterAircraftUseCase registerAircraftUseCase;
    @Autowired private RegisterAircraftModelUseCase registerModelUseCase;
    @Autowired private AircraftRepository aircraftRepository;
    @Autowired private EntityManager entityManager; // Added to manage JPA cache

    @Test
    void shouldUpdateStatusInDatabase() {
        registerModelUseCase.execute(new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "B737", 20000, 5000, 800));
        registerAircraftUseCase.execute(new RegisterAircraftRequest("CS-UPD1", "B737", LocalDate.now(), 180, AircraftStatus.AVAILABLE));

        UpdateAircraftStatusRequest patchRequest = new UpdateAircraftStatusRequest(AircraftStatus.IN_FLIGHT);
        updateUseCase.execute("CS-UPD1", patchRequest);

        Aircraft aircraft = aircraftRepository.findByRegistrationNumber(new RegistrationNumber("CS-UPD1")).get();
        assertEquals(AircraftStatus.IN_FLIGHT, aircraft.getCurrentStatus());
    }

    @Test
    void shouldFailIfAircraftDoesNotExist() {
        UpdateAircraftStatusRequest patchRequest = new UpdateAircraftStatusRequest(AircraftStatus.IN_FLIGHT);
        assertThrows(IllegalArgumentException.class, () -> updateUseCase.execute("CS-FAKE", patchRequest));
    }

    @Test
    void shouldThrowExceptionOnConcurrentUpdate() {
        // Arrange: Criar o avião na BD (versão 0)
        registerModelUseCase.execute(new RegisterAircraftModelRequest(AircraftManufacturer.BOEING, "A320", 20000, 5000, 800));
        registerAircraftUseCase.execute(new RegisterAircraftRequest("CS-UPD2", "A320", LocalDate.now(), 180, AircraftStatus.AVAILABLE));

        // Act: O Utilizador 1 vai buscar o avião (versão 0)
        Aircraft user1Aircraft = aircraftRepository.findByRegistrationNumber(new RegistrationNumber("CS-UPD2")).get();
        // Retiramos o Utilizador 1 do cache para a JPA não o atualizar nas nossas costas!
        entityManager.detach(user1Aircraft);

        // O Utilizador 2 vai buscar o avião (versão 0)
        Aircraft user2Aircraft = aircraftRepository.findByRegistrationNumber(new RegistrationNumber("CS-UPD2")).get();

        // O Utilizador 2 despacha-se e grava primeiro!
        user2Aircraft.updateStatus(AircraftStatus.IN_FLIGHT);
        aircraftRepository.saveAndFlush(user2Aircraft); // A BD agora está na versão 1

        // O Utilizador 1 (que esteve muito tempo a pensar) tenta gravar a sua cópia antiga
        user1Aircraft.updateStatus(AircraftStatus.UNDER_MAINTENANCE);

        // Assert: O Spring deteta que o Utilizador 1 tem a versão 0, mas a BD já vai na versão 1!
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            aircraftRepository.saveAndFlush(user1Aircraft);
        });
    }
}