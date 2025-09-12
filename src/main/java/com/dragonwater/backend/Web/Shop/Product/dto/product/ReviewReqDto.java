package com.dragonwater.backend.Web.Shop.Product.dto.product;

import lombok.Data;
import lombok.Getter;

@Data
public class ReviewReqDto {
    private Long productId;
    private String orderName;
    private Integer rating;
    private String comment;
}
