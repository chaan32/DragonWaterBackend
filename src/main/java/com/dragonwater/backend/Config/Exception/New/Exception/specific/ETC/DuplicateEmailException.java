package com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class DuplicateEmailException extends ServiceException {
    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL, ErrorCode.DUPLICATE_EMAIL.getMessage());
    }
}
