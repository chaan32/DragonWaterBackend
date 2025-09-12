package com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;


public class DuplicateClaimException extends ServiceException {
    public DuplicateClaimException() {
        super(ErrorCode.DUPLICATE_CLAIM, ErrorCode.DUPLICATE_CLAIM.getMessage());
    }

    public DuplicateClaimException(String message) {
        super(ErrorCode.DUPLICATE_CLAIM, message);
    }
}