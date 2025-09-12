package com.dragonwater.backend.Web.Shop.Product.dto.product;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayProductResDto {
    private ShowProductsResDto bestProduct;
    private ShowProductsResDto newProduct;
    private ShowProductsResDto recommendationProduct;

    public static DisplayProductResDto of(ShowProductsResDto bestProduct,
                                          ShowProductsResDto newProduct,
                                          ShowProductsResDto recommendationProduct){
        return DisplayProductResDto.builder()
                .bestProduct(bestProduct)
                .newProduct(newProduct)
                .recommendationProduct(recommendationProduct)
                .build();
    }
}
