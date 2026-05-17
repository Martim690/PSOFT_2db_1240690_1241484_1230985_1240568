package psoft_aisafe.security.application;

import psoft_aisafe.security.application.dtos.AuthResponse;
import psoft_aisafe.security.application.dtos.RegisterRequest;
import psoft_aisafe.security.domain.User;
import psoft_aisafe.security.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterUserUseCase(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse execute(RegisterRequest request) {
        var user = new User(
                request.username(),
                passwordEncoder.encode(request.password()) // Encriptar a password!
        );

        if (request.roles() != null) {
            request.roles().forEach(user::addRole);
        }

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
