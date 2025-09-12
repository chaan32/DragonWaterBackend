package com.dragonwater.backend.Web.User.Auth.dto.Approve;

import lombok.Getter;

@Getter
public class RequestVerifyPhoneDto {
    private String phone;
    private String verifyCode;
}
