package com.dragonwater.backend.Config.Exception.New.Exception.type;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.BusinessException;

public class EntityException extends BusinessException {
    public EntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
