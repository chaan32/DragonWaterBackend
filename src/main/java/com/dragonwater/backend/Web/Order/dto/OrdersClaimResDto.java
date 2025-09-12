package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.Orders;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrdersClaimResDto {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime createdAt;

    public static OrdersClaimResDto of(Orders orders) {
        return OrdersClaimResDto.builder()
                .orderId(orders.getId())
                .orderNumber(orders.getOrderNumber())
                .createdAt(orders.getCreatedAt())
                .build();
    }
}
