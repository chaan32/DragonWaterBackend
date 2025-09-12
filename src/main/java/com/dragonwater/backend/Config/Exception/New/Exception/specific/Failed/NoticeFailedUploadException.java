package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;

public class NoticeFailedUploadException extends ServiceException {
    public NoticeFailedUploadException() {
        super(ErrorCode.NOTICE_UPLOAD_FAILED);
    }
}
