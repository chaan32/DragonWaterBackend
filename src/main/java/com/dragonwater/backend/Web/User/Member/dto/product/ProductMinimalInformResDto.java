package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.domain.SpecializeProducts;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import com.dragonwater.backend.Web.User.Member.dto.search.HQInformResDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductMinimalInformResDto {
    private Long id;
    private String name;
    private String imageUrl;

    public static ProductMinimalInformResDto of(Products product) {
        String imageUrl = "";
        if (product.getDescription() != null) {
            imageUrl = product.getDescription().getThumbnailImageS3URL();
        }
        return ProductMinimalInformResDto.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(imageUrl)
                .build();
    }

    public static ProductMinimalInformResDto adminOf(SpecializeProducts product) {
        String imageUrl = "";
        if (product.getProduct().getDescription() != null) {
            imageUrl = product.getProduct().getDescription().getThumbnailImageS3URL();
        }
        return ProductMinimalInformResDto.builder()
                .id(product.getId())
                .name(product.getProduct().getName())
                .imageUrl(imageUrl)
                .build();
    }
}
