package com.finance.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ---- Sign Up ----
public class AuthRequest {

    @Data
    public static class SignUp {
        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank @Email(message = "Valid email is required")
        private String email;

        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
    }

    @Data
    public static class Login {
        @NotBlank @Email
        private String email;

        @NotBlank
        private String password;
    }

    @Data
    public static class RefreshToken {
        @NotBlank
        private String refreshToken;
    }
}
