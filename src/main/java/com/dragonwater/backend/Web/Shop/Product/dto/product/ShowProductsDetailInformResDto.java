package com.dragonwater.backend.Web.Shop.Product.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductImages;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ShowProductsDetailInformResDto {
    // 쇼핑몰에서 제품의 상세보기를 누르면 보이는 페이지에 나오는 정보를 모두 제공
    private Long productId;
    private String productName;
    private Integer customerPrice;
    private Integer businessPrice;
    private Integer discountPrice;
    private Integer discountPercent;
    private Integer shippingFee;
    private Float rating;
    private Integer reviewCount;
    private String salesStatus;
    private String thumbnailImageUrl;
    private List<String> galleryImageUrls;
    private String htmlContent;
    private String title;

    public static ShowProductsDetailInformResDto of(Products products) {
        List<String> urls = new LinkedList<>();
        ProductDescription description = products.getDescription();

        List<ProductImages> galleryImages = description.getGalleryImages();
        for (ProductImages galleryImage : galleryImages) {
            urls.add(galleryImage.getS3URL());
        }

        Float r = calculateRating(products.getComments());
        return ShowProductsDetailInformResDto.builder()
                .productId(products.getId())
                .productName(products.getName())
                .customerPrice(products.getCustomerPrice())
                .businessPrice(products.getBusinessPrice())
                .discountPercent(products.getDiscountPrice())
                .discountPercent(products.getDiscountPercent())
                .shippingFee(3000)
                .rating(r)
                .reviewCount(products.getComments().size())
                .salesStatus(products.getSalesStatus().getDescription())
                .thumbnailImageUrl(description.getThumbnailImageS3URL())
                .galleryImageUrls(urls)
                .htmlContent(description.getDescription())
                .title(products.getDescription().getTitle())
                .build();
    }

    private static Float calculateRating(List<Comments> comments) {
        int size = comments.size();
        if (size == 0) return 0.0F;
        int sum = 0;
        for (Comments comment : comments) {
            sum += comment.getRating();
        }
        float avg = (float) sum / size;
        return Math.round(avg*10) / 10.0f;
    }

}
