package com.dragonwater.backend.Web.Shop.Product.dto.category;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowCategoriesResDto {
    private String categoryName;
    private Long categoryId;

    public static ShowCategoriesResDto of(ProductCategories category) {
        return ShowCategoriesResDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategory())
                .build();
    }
}
