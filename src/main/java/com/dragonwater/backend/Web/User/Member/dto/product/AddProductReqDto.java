package com.dragonwater.backend.Web.User.Member.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class AddProductReqDto {
    private String name; // 제품명
    private Integer businessPrice;
    private Integer customerPrice;
    private Integer discountPrice;
    private Integer discountPercent;
    private Integer stock;
    private Long categoryId;
    private Long mainCategoryId;
    private List<String> expressions;
}

