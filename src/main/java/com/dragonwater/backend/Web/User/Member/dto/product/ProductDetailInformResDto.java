package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductExpression;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class ProductDetailInformResDto {
    private Long id;
    private String name;
    private String subCategory;
    private String mainCategory;
    private Long categoryId;
    private Integer customerPrice;
    private Integer businessPrice;
    private Integer discountPrice;
    private Integer discountPercent;
    private Integer stock;
    private String status;
    private LocalDateTime createdAt;
    private ConcurrentHashMap<Long, String> expressions;
    private Boolean isNew;
    private Boolean isRecommendation;
    private Boolean isBest;

    public static ProductDetailInformResDto of(Products product) {
        ConcurrentHashMap<Long, String> ex = new ConcurrentHashMap<>();
        for (ProductExpression expression : product.getExpressions()) {
            ex.put(expression.getId(), expression.getExpression());
        }
        return ProductDetailInformResDto.builder()
                .name(product.getName())
                .categoryId(product.getCategory().getId())
                .mainCategory(product.getCategory().getMainCategory().getCategory())
                .subCategory(product.getCategory().getCategory())
                .customerPrice(product.getCustomerPrice())
                .businessPrice(product.getBusinessPrice())
                .discountPercent(product.getDiscountPercent())
                .discountPrice(product.getDiscountPrice())
                .stock(product.getStock())
                .status(product.getSalesStatus().getDescription())
                .createdAt(product.getCreatedAt())
                .id(product.getId())
                .expressions(ex)
                .isRecommendation(product.getIsRecommendation())
                .isNew(product.getIsNew())
                .isBest(product.getIsBest())
                .build();
    }
}

