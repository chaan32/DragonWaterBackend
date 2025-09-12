package com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed;

import com.dragonwater.backend.Config.Exception.New.ErrorCode;
import com.dragonwater.backend.Config.Exception.New.Exception.type.ServiceException;


public class OrderFailByStockException extends ServiceException {
    public OrderFailByStockException() {
        super(ErrorCode.ORDER_FAIL_STOCK);
    }
    public OrderFailByStockException(String message) {
        super(ErrorCode.ORDER_FAIL_STOCK, message);
    }
}
