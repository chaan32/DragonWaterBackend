package com.dragonwater.backend.Config.Exception.New.Exception.specific.Token;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.AuthenticationException;

public class AuthenticationRequiredException extends AuthenticationException {
    public AuthenticationRequiredException() {
        super(ErrorCode.AUTHENTICATION_REQUIRED);
    }
}
