package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class SendSMSFailedException extends ServiceException {
    public SendSMSFailedException() {
        super(ErrorCode.FAILED_SEND_SMS);
    }
}
