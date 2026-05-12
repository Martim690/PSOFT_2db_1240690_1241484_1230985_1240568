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
        // 1. Criar a nova instância de utilizador
        var user = new User(
                request.username(),
                passwordEncoder.encode(request.password()) // Encriptar a password!
        );

        // 2. Adicionar as roles pedidas
        if (request.roles() != null) {
            request.roles().forEach(user::addRole);
        }

        // 3. Guardar na Base de Dados
        repository.save(user);

        // 4. Gerar e devolver o token
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
