package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;
import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class DeleteFailedException extends ServiceException {
    public DeleteFailedException() {
        super(ErrorCode.DELETE_FAILED, "삭제에 실패했습니다.");
    }
}
