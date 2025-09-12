package com.dragonwater.backend.Web.Order.service.impl;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.OrderFailByStockException;
import com.dragonwater.backend.Web.Order.dto.OrderItemDto;
import com.dragonwater.backend.Web.Order.service.interf.StockService;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    // 인터페이스 구현 완
    private final ProductService productService;

    /**
     * 주문 아이템 목록 전체에 대한 재고를 하나의 새로운 트랜잭션으로 처리
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStockForOrder(List<OrderItemDto> itemDtos) {
        for (OrderItemDto item : itemDtos) {
            Products product = productService.getProductByIdWithLock(item.getProductId());
            if (!product.order(item.getQuantity())) {
                throw new OrderFailByStockException(product.getName()+"의 현재 재고는 "+product.getStock()+"개 입니다.");
            }
        }
    }
}
