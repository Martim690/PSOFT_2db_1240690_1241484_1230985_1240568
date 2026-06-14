package psoft_aisafe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import psoft_aisafe.aircrafts.domain.*;
import psoft_aisafe.routes.domain.Route;
import psoft_aisafe.routes.domain.RouteRequirements;
import psoft_aisafe.routes.domain.RouteRepository;
import psoft_aisafe.security.domain.Role;
import psoft_aisafe.security.domain.User;
import psoft_aisafe.security.domain.UserRepository;

import java.time.LocalDate;

@Component
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AircraftModelRepository aircraftModelRepository;
    private final AircraftRepository aircraftRepository;
    private final RouteRepository routeRepository;

    public Bootstrap(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AircraftModelRepository aircraftModelRepository,
                     AircraftRepository aircraftRepository,
                     RouteRepository routeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.aircraftModelRepository = aircraftModelRepository;
        this.aircraftRepository = aircraftRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    //Users
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("admin123"));
            admin.addRole(Role.ADMIN);
            userRepository.save(admin);
        }
        if (userRepository.findByUsername("backoffice").isEmpty()) {
            User backoffice = new User("backoffice", passwordEncoder.encode("backoffice123"));
            backoffice.addRole(Role.BACKOFFICE_OPERATOR);
            userRepository.save(backoffice);
        }
        if (userRepository.findByUsername("atcc").isEmpty()) {
            User atcc = new User("atcc", passwordEncoder.encode("atcc123"));
            atcc.addRole(Role.ATCC);
            userRepository.save(atcc);
        }
//Aircraft Model
        AircraftModel airbusA350 = aircraftModelRepository.findByModelName("A350-900")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("A350-900", 141000, 15000, 903, AircraftManufacturer.AIRBUS, "http://localhost:8080/diagrams/a350-900.png")));

        AircraftModel boeing787 = aircraftModelRepository.findByModelName("B787-9")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("B787-9", 126220, 14140, 903, AircraftManufacturer.BOEING, "http://localhost:8080/diagrams/b787-9.png")));

        AircraftModel embraerE195 = aircraftModelRepository.findByModelName("E195-E2")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("E195-E2", 13690, 4815, 870, AircraftManufacturer.EMBRAER, "http://localhost:8080/diagrams/e195-e2.png")));

//Aircraft
        RegistrationNumber regA350 = new RegistrationNumber("CS-TXA");
        if (aircraftRepository.findByRegistrationNumber(regA350).isEmpty()) {
            // Adicionado: 4500 horas, 300 tarefas no final
            aircraftRepository.save(new Aircraft(regA350, airbusA350, LocalDate.of(2021, 3, 15), 300, AircraftStatus.AVAILABLE, 4500, 300));
        }

        RegistrationNumber regB787 = new RegistrationNumber("CS-TZE");
        if (aircraftRepository.findByRegistrationNumber(regB787).isEmpty()) {
            // Adicionado: 3200 horas, 220 tarefas no final
            aircraftRepository.save(new Aircraft(regB787, boeing787, LocalDate.of(2019, 8, 24), 290, AircraftStatus.IN_FLIGHT, 3200, 220));
        }

        RegistrationNumber regE195 = new RegistrationNumber("CS-TTY");
        if (aircraftRepository.findByRegistrationNumber(regE195).isEmpty()) {
            // Adicionado: 600 horas, 210 tarefas no final
            aircraftRepository.save(new Aircraft(regE195, embraerE195, LocalDate.of(2023, 5, 10), 136, AircraftStatus.AVAILABLE, 600, 210));
        }
        // 4. Routes Bootstrapping (Para habilitar US203.md e relatórios)
        // Rota Curta: Lisboa -> Porto (Compatível com todos)
        if (!routeRepository.existsActiveRouteBetween("LIS", "OPO")) {
            RouteRequirements reqShort = new RouteRequirements(400, 50, "CAT1");
            routeRepository.save(new Route("LIS", "OPO", reqShort, 300.0, 50));
        }

        // Rota Média: Lisboa -> Paris CDG (Compatível com todos)
        if (!routeRepository.existsActiveRouteBetween("LIS", "CDG")) {
            RouteRequirements reqMedium = new RouteRequirements(2000, 120, "CAT2");
            routeRepository.save(new Route("LIS", "CDG", reqMedium, 1700.0, 150));
        }

        // Rota de Longo Curso Exigente: Lisboa -> New York JFK
        // (A320neo tem range 6300km (OK), mas o B737-800 tem 5436km (Incompatível se a distância real exigir mais alcance ou margem))
        if (!routeRepository.existsActiveRouteBetween("LIS", "JFK")) {
            RouteRequirements reqLong = new RouteRequirements(5600, 150, "CAT3");
            routeRepository.save(new Route("LIS", "JFK", reqLong, 5400.0, 480));
        }
    }
}