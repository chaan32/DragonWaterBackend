package com.dragonwater.backend.Web.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal perPrice;

}
