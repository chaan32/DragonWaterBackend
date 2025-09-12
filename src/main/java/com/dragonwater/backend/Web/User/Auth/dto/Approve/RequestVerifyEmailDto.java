package com.dragonwater.backend.Web.User.Auth.dto.Approve;

import lombok.Getter;

@Getter
public class RequestVerifyEmailDto {
    private String email;
    private String verifyCode;
}
