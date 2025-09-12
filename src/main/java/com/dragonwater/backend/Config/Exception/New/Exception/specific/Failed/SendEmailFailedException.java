package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class SendEmailFailedException extends ServiceException {
    public SendEmailFailedException(String message) {
        super(ErrorCode.FAILED_SEND_EMAIL);
    }
}
