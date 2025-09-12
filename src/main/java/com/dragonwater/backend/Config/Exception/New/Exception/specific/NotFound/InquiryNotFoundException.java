package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class InquiryNotFoundException extends EntityException {
    public InquiryNotFoundException() {
        super(ErrorCode.INQUIRY_NOT_FOUND, "문의를 찾을 수 없습니다.");
    }
}
