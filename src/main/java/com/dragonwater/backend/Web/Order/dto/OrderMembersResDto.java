package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class OrderMembersResDto {
    private String orderNumber;
    private Long orderId;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal shipmentFee;

    public static OrderMembersResDto of(Orders order) {
        List<OrderItems> items = order.getItems();
        String productName = items.getFirst().getProduct().getName();
        int size = items.size();
        if (size != 1) {
            productName = productName + " 외 " + (size-1) ;
        }
        return OrderMembersResDto.builder()
                .shipmentFee(order.getShipmentFee())
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .productName(productName)
                .productPrice(order.getProductPrice())
                .build();
    }
}
