package com.dragonwater.backend.Web.Shop.Product.dto.expression;

import lombok.Data;

@Data
public class ExpressionAddReqDto {
    private Long productId;
    private String expression;
}
