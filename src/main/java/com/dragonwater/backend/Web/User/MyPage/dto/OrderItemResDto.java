package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResDto {
    private String productName;
    private Long productId;
    private BigDecimal productPerPrice;
    private BigDecimal productTotalPrice;
    private Integer quantity;
    private String productImageUrl;

    public static OrderItemResDto of(OrderItems item) {
        return OrderItemResDto.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productPerPrice(item.getPerPrice())
                .productTotalPrice(item.getTotalPrice())
                .quantity(item.getQuantity())
                .productImageUrl(item.getProduct().getDescription().getThumbnailImageS3URL())
                .build();
    }
}