package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;


public class ProductNotFoundException extends EntityException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND, "제품을 찾을 수 없습니다.");
    }
}