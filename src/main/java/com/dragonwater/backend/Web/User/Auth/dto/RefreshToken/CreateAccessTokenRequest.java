package com.dragonwater.backend.Web.User.Auth.dto.RefreshToken;

import lombok.Data;

@Data
public class CreateAccessTokenRequest {
    private String refreshToken;
}
