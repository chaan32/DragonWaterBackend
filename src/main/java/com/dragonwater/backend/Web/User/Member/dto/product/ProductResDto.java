package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResDto {
    private Long id;
    private String name;

    public static ProductResDto of(Products products) {
        return ProductResDto.builder()
                .id(products.getId())
                .name(products.getName())
                .build();
    }
}
