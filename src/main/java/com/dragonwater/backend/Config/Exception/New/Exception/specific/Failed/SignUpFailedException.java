package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class SignUpFailedException extends ServiceException {
    public SignUpFailedException(String reason) {
        super(ErrorCode.SIGNUP_FAILED, reason);
    }
}
