package psoft_aisafe.security.application.dtos;

import jakarta.validation.constraints.NotBlank;
import psoft_aisafe.security.domain.Role;
import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        Set<Role> roles
) {}
