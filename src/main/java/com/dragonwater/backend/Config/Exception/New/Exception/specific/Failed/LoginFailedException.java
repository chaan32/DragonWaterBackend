package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.AuthenticationException;

public class LoginFailedException extends AuthenticationException {
    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }
}
