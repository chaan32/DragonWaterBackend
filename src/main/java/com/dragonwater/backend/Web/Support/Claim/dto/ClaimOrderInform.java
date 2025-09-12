package com.dragonwater.backend.Web.Support.Claim.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Slf4j
@Data
public class ClaimOrderInform {
    private Long orderId;
    private String orderNumber;
    private BigDecimal productPrice;
    private String productName;
    private BigDecimal shipmentFee;

    public static ClaimOrderInform of(Orders order) {
        log.info("orderId : {}", order.getId());
        List<OrderItems> items = order.getItems();

        String productName = items.getFirst().getProduct().getName();
        if (items.size() != 1) {
            productName += "외" + (items.size() - 1) + "종";
        }
        return ClaimOrderInform.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .productPrice(order.getProductPrice())
                .shipmentFee(order.getShipmentFee())
                .productName(productName)
                .build();
    }
}
