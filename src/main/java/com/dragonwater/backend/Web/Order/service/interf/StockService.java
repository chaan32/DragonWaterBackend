package com.dragonwater.backend.Web.Order.service.interf;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.OrderFailByStockException;
import com.dragonwater.backend.Web.Order.dto.OrderItemDto;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StockService {

    /**
     * 재고를 줄이는 메소드
     * @param itemDtos 주문 리스트
     */
    void decreaseStockForOrder(List<OrderItemDto> itemDtos);
}
