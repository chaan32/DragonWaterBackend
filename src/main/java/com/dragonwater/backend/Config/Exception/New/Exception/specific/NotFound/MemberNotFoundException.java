package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class MemberNotFoundException extends EntityException {
    public MemberNotFoundException(Long memberId) {
        super(ErrorCode.MEMBER_NOT_FOUND, "ID: " + memberId + "에 해당하는 회원을 찾을 수 없습니다.");
    }
    public MemberNotFoundException(String memberLoginId) {
        super(ErrorCode.MEMBER_NOT_FOUND, "ID: " + memberLoginId + "에 해당하는 회원을 찾을 수 없습니다.");
    }
}
