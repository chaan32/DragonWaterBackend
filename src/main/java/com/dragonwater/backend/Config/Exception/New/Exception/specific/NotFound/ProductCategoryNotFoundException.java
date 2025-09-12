package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;


public class ProductCategoryNotFoundException extends EntityException {
    public ProductCategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND, "제품의 카테고리 찾을 수 없습니다.");
    }
}