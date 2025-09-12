package com.dragonwater.backend.Config.Exception.New.Exception.specific.Token;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.AuthenticationException;



public class ExpiredAccessTokenException extends AuthenticationException {
    public ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_ACCESS_TOKEN);
    }
}