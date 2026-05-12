package psoft_aisafe.security.application;

import psoft_aisafe.security.application.dtos.AuthResponse;
import psoft_aisafe.security.application.dtos.LoginRequest;
import psoft_aisafe.security.domain.InvalidCredentialsException;
import psoft_aisafe.security.domain.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticateUserUseCase(UserRepository repository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse execute(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }

        var user = repository.findByUsername(request.username()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
