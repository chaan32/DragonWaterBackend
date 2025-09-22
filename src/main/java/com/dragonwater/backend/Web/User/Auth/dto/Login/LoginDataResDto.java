package com.dragonwater.backend.Web.User.Auth.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDataResDto {
    private String accessToken;
    private String secretToken;
    private LoginSuccessResDto user;
    private Integer expiresIn = 1000000;

    public static LoginDataResDto of(String accessToken, String secretToken, LoginSuccessResDto user){
        return LoginDataResDto.builder()
                .accessToken(accessToken)
                .secretToken(secretToken)
                .user(user)
                .build();
    }
}
