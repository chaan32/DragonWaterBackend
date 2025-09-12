package com.dragonwater.backend.Web.Shop.Product.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductExpression;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.domain.SalesStatus;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowCategoriesResDto;
import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ShowProductsResDto {
    private Long productId;
    private Long categoryId;
    private String productName;
    private String mainCategory;
    private String subCategory;
    private Integer customerPrice;
    private Integer businessPrice;
    private String thumbnailImageUrl;
    private List<String> expressions;
    private Float rating;
    private Integer reviews;
    private SalesStatus salesStatus;
    private Boolean isBest;
    private Boolean isNew;
    private Boolean isRecommendation;



    public static ShowProductsResDto of(Products products) {

        List<String> expressions = new LinkedList<>();
        for (ProductExpression expression : products.getExpressions()) {
            expressions.add(expression.getExpression());
        }
        return ShowProductsResDto.builder()
                .productId(products.getId())
                .categoryId(products.getCategory().getId())
                .productName(products.getName())
                .subCategory(products.getCategory().getCategory())
                .mainCategory(products.getCategory().getMainCategory().getCategory())
                .customerPrice(products.getCustomerPrice())
                .businessPrice(products.getBusinessPrice())
                .thumbnailImageUrl(products.getDescription().getThumbnailImageS3URL())
                .expressions(expressions)
                .rating(products.getRating())
                .reviews(products.getComments().size())
                .salesStatus(products.getSalesStatus())
                .isBest(products.getIsBest())
                .isRecommendation(products.getIsRecommendation())
                .isNew(products.getIsNew())
                .build();
    }
}
