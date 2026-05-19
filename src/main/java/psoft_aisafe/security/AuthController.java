package psoft_aisafe.security;

import psoft_aisafe.security.application.AuthenticateUserUseCase;
import psoft_aisafe.security.application.RegisterUserUseCase;
import psoft_aisafe.security.application.dtos.AuthResponse;
import psoft_aisafe.security.application.dtos.LoginRequest;
import psoft_aisafe.security.application.dtos.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    private final RegisterUserUseCase registerUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, RegisterUserUseCase registerUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(registerUserUseCase.execute(request));
    }
}