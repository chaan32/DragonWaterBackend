package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class NoticeNotFoundException extends EntityException {
    public NoticeNotFoundException() {
        super(ErrorCode.NOTICE_NOT_FOUND, "공지사항을 찾을 수 없습니다.");
    }
}
