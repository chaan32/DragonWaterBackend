package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductResDto {
    private Long id;
    private String name;
    private String message;

    public static AddProductResDto of(Products products) {
        return AddProductResDto.builder()
                .id(products.getId())
                .name(products.getName())
                .build();
    }
}
