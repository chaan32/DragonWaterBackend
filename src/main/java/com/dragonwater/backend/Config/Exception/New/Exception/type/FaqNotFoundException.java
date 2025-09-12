package com.dragonwater.backend.Config.Exception.New.Exception.type;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;

public class FaqNotFoundException extends EntityException {
    public FaqNotFoundException() {
        super(ErrorCode.FAQ_NOT_FOUND, "FAQ를 찾을 수 없습니다.");
    }
}
