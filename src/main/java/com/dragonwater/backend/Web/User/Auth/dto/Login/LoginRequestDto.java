package com.dragonwater.backend.Web.User.Auth.dto.Login;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username; // 로그인 아이디
    private String password; // 로그인 비밀번호

    private DeviceInfoDto deviceInfo;
}
