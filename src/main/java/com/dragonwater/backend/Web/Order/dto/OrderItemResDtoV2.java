package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderItemResDtoV2 {
    private Long orderItemId;
    private Long productId;
    private Integer quantity;
    private BigDecimal perPrice;
    private String productName;
    private String productImageUrl;

    public static OrderItemResDtoV2 of(OrderItems orderItems) {
        return OrderItemResDtoV2.builder()
                .orderItemId(orderItems.getId())
                .productId(orderItems.getProduct().getId())
                .quantity(orderItems.getQuantity())
                .perPrice(orderItems.getPerPrice())
                .productName(orderItems.getProduct().getName())
                .productImageUrl(orderItems.getProduct().getDescription().getThumbnailImageS3URL())
                .build();
    }
}
