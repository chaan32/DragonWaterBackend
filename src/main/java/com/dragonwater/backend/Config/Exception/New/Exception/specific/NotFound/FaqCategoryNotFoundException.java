package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;

public class FaqCategoryNotFoundException extends EntityException {
    public FaqCategoryNotFoundException() {
        super(ErrorCode.FAQ_CATEGORY_NOT_FOUND, "FAQ 카테고리를 찾을 수 없습니다.");
    }
}
