package psoft_aisafe.security;

import psoft_aisafe.security.application.AuthenticateUserUseCase;
import psoft_aisafe.security.application.dtos.AuthResponse;
import psoft_aisafe.security.application.dtos.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request));
    }
}