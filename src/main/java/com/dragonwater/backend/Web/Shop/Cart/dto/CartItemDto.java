package com.dragonwater.backend.Web.Shop.Cart.dto;

import com.dragonwater.backend.Web.Shop.Cart.domain.CartItems;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private Integer quantity;
    private Long productId;

    public static CartItemDto of(CartItems items) {
        return CartItemDto.builder()
                .productId(items.getProduct().getId())
                .quantity(items.getQuantity())
                .build();
    }
}
