package com.dragonwater.backend.Web.Shop.Product.dto.category;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ShowMainAndSubCategoriesResDto {
    private Long mainCategoryId;
    private String mainCategory;
    private List<ShowCategoriesResDto> subCategories;

    public static ShowMainAndSubCategoriesResDto of(ProductMainCategories productMainCategories) {
        List<ShowCategoriesResDto> dtos = new LinkedList<>();
        for (ProductCategories productCategories : productMainCategories.getSubCategories()) {
            dtos.add(ShowCategoriesResDto.of(productCategories));
        }
        return ShowMainAndSubCategoriesResDto.builder()
                .mainCategoryId(productMainCategories.getId())
                .mainCategory(productMainCategories.getCategory())
                .subCategories(dtos)
                .build();
    }
}
