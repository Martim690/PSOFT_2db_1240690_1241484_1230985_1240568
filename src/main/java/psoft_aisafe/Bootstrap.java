package psoft_aisafe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import psoft_aisafe.aircrafts.domain.*;
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

    public Bootstrap(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AircraftModelRepository aircraftModelRepository,
                     AircraftRepository aircraftRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.aircraftModelRepository = aircraftModelRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // --- A TUA PARTE DOS UTILIZADORES ---
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

        AircraftModel boeing737 = aircraftModelRepository.findByModelName("B737-800")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("B737-800", 26020, 5436, 842, AircraftManufacturer.BOEING)));

        AircraftModel airbusA320 = aircraftModelRepository.findByModelName("A320neo")
                .orElseGet(() -> aircraftModelRepository.save(new AircraftModel("A320neo", 26730, 6300, 833, AircraftManufacturer.AIRBUS)));

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
    }
}