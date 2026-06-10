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
        AircraftModel boeing737 = aircraftModelRepository.findByModelName("B737-800")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("B737-800", 26020, 5436, 842, AircraftManufacturer.BOEING)));

        AircraftModel airbusA320 = aircraftModelRepository.findByModelName("A320neo")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("A320neo", 26730, 6300, 833, AircraftManufacturer.AIRBUS)));
//Aircraft
        RegistrationNumber reg1 = new RegistrationNumber("CS-TPA");
        if (aircraftRepository.findByRegistrationNumber(reg1).isEmpty()) {
            aircraftRepository.save(new Aircraft(reg1, boeing737, LocalDate.of(2018, 5, 20), 180, AircraftStatus.AVAILABLE));
        }

        RegistrationNumber reg2 = new RegistrationNumber("CS-TQB");
        if (aircraftRepository.findByRegistrationNumber(reg2).isEmpty()) {
            aircraftRepository.save(new Aircraft(reg2, boeing737, LocalDate.of(2020, 11, 15), 180, AircraftStatus.IN_FLIGHT));
        }

        RegistrationNumber reg3 = new RegistrationNumber("CS-AVA");
        if (aircraftRepository.findByRegistrationNumber(reg3).isEmpty()) {
            aircraftRepository.save(new Aircraft(reg3, airbusA320, LocalDate.of(2022, 1, 10), 174, AircraftStatus.UNDER_MAINTENANCE));
        }
        // 4. Routes Bootstrapping (Para habilitar US203 e relatórios)
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