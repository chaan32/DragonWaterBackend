package com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.AuthenticationException;

public class ApprovalPendingException extends AuthenticationException {
    public ApprovalPendingException() {
        super(ErrorCode.APPROVAL_PENDING);
    }
}
