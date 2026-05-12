package psoft_aisafe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import psoft_aisafe.security.domain.Role;
import psoft_aisafe.security.domain.User;
import psoft_aisafe.security.domain.UserRepository;

@Component
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Verifica se o admin já existe, se não, cria-o!
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("admin123"));
            admin.addRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}