package com.dragonwater.backend.Web.Shop.Product.dto.expression;


import lombok.Data;

@Data
public class ExpressionDeleteReqDto {
    private Long productId;
    private Long expressionId;

}
