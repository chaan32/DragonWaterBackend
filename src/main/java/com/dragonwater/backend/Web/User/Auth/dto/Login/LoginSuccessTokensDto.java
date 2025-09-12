package com.dragonwater.backend.Web.User.Auth.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginSuccessTokensDto {
    private String accessToken;
    private String refreshToken;

    public static LoginSuccessTokensDto of(String accessToken, String refreshToken){
        return LoginSuccessTokensDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
