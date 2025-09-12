package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class ClaimNotFoundException extends EntityException {
    public ClaimNotFoundException() {
        super(ErrorCode.CLAIM_NOT_FOUND, "클레임을 찾을 수 없습니다.");
    }
}
