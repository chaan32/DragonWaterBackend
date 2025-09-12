package com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.EntityException;


public class OrderNotFoundException extends EntityException {
    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND, "주문을 찾을 수 없습니다.");
    }
}