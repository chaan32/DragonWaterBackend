package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class EditFailedException extends ServiceException {
    public EditFailedException() {
        super(ErrorCode.EDIT_FAILED, "수정에 실패했습니다.");
    }
}
