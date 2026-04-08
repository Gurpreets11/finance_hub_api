package com.finance.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TokenPair {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private long expiresIn;       // seconds
        private UserInfo user;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String fullName;
        private String email;
        private String role;
        private LocalDateTime createdAt;
    }
}
