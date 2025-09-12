package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MPBranchOrderItemsDto {
    private Long id;
    private Long orderId;
    private String productName;
    private Integer quantity;
    private BigDecimal productTotalPrice;
    private BigDecimal productPerPrice;

    public static MPBranchOrderItemsDto of(OrderItems orderItems) {
        return MPBranchOrderItemsDto.builder()
                .id(orderItems.getId())
                .orderId(orderItems.getOrder().getId())
                .quantity(orderItems.getQuantity())
                .productPerPrice(orderItems.getPerPrice())
                .productTotalPrice((orderItems.getTotalPrice()))
                .productName(orderItems.getProduct().getName())
                .build();
    }
}
