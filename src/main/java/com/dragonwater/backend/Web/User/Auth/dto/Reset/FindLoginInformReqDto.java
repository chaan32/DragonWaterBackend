package com.dragonwater.backend.Web.User.Auth.dto.Reset;

import lombok.Getter;

@Getter
public class FindLoginInformReqDto {
    private String method;
    private String value;
    private String loginId;
    private String verifyCode;
}
