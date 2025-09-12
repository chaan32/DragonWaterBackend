package com.dragonwater.backend.Web.User.Member.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class EditProductReqDto {
    private Long id;
    private Long categoryId;
    private String name;
    private Integer customerPrice;
    private Integer businessPrice;
    private Integer discountPrice;
    private Integer discountPercent;
    private Integer stock;
    private String status;
    private Boolean isNew;
    private Boolean isRecommendation;
    private Boolean isBest;
}
