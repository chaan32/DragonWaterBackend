package com.dragonwater.backend.Web.Shop.Product.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDisplay;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConstructMainPageResDto {
    private Long bestProducts;
    private Long newProducts;
    private Long recommendedProducts;

    public static ConstructMainPageResDto of(ProductDisplay productDisplay) {
        return ConstructMainPageResDto.builder()
                .bestProducts(productDisplay.getBestProductId())
                .newProducts(productDisplay.getNewProductId())
                .recommendedProducts((productDisplay.getRecommendationProductId()))
                .build();
    }
}
