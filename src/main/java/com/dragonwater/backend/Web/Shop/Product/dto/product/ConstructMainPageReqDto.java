package com.dragonwater.backend.Web.Shop.Product.dto.product;

import lombok.Data;

@Data
public class ConstructMainPageReqDto {
    private Long bestProducts;
    private Long newProducts;
    private Long recommendedProducts;
}
