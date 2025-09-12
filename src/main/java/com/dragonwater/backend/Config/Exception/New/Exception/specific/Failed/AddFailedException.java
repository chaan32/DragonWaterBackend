package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class AddFailedException extends ServiceException {
    public AddFailedException() {
        super(ErrorCode.ADD_FAILED, "추가에 실패했습니다. ");
    }
}
