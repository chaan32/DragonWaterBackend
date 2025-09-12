package com.dragonwater.backend.Config.Exception.New.Exception.type;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.BusinessException;

public class ServiceException extends BusinessException {
    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
