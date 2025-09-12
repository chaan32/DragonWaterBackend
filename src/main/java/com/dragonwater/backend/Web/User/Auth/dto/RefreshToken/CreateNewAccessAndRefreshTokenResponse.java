package com.dragonwater.backend.Web.User.Auth.dto.RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@Builder
public class CreateNewAccessAndRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;

    public static CreateNewAccessAndRefreshTokenResponse of(ConcurrentHashMap<String, String> tokens) {
        return CreateNewAccessAndRefreshTokenResponse.builder()
                .accessToken(tokens.get("accessToken"))
                .refreshToken(tokens.get("refreshToken"))
                .build();
    }
}
