package com.dragonwater.backend.Web.Support.Inquiry.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemInqResDto {
    private String thumbnailUrl;
    private String productName;
    private Integer quantity;

    public static OrderItemInqResDto of(OrderItems orderItems) {
        return OrderItemInqResDto.builder()
                .productName(orderItems.getProduct().getName())
                .quantity(orderItems.getQuantity())
                .thumbnailUrl(orderItems.getProduct().getDescription().getThumbnailImageS3URL())
                .build();
    }
}
