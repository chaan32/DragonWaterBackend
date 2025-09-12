package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class CategoryResDto {
    private Long id;
    private String category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConcurrentHashMap<Long, String> subCategories;

    public static CategoryResDto of(ProductCategories dto) {
        return CategoryResDto.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .build();
    }

    public static CategoryResDto of(ProductMainCategories dto) {
        ConcurrentHashMap<Long, String> subCategories = new ConcurrentHashMap<>();
        for (ProductCategories productCategories : dto.getSubCategories()) {
            subCategories.put(productCategories.getId(), productCategories.getCategory());
        }
        return CategoryResDto.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .subCategories(subCategories)
                .build();
    }
}
