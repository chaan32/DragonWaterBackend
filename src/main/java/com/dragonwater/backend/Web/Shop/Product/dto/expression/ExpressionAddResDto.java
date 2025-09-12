package com.dragonwater.backend.Web.Shop.Product.dto.expression;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductExpression;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpressionAddResDto {
    private Long id;
    private String expression;

    public static ExpressionAddResDto of(ProductExpression productExpression) {
        return ExpressionAddResDto.builder()
                .id(productExpression.getId())
                .expression(productExpression.getExpression())
                .build();
    }
}
