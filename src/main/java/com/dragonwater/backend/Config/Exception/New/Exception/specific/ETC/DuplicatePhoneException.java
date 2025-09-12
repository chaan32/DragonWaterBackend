package com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;


public class DuplicatePhoneException extends ServiceException {
    public DuplicatePhoneException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }
    public DuplicatePhoneException() {
        super(ErrorCode.DUPLICATE_EMAIL, ErrorCode.DUPLICATE_EMAIL.getMessage());
    }
}
