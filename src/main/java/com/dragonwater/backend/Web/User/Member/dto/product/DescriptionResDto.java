package com.dragonwater.backend.Web.User.Member.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductImages;
import lombok.Builder;
import lombok.Data;
import org.aspectj.weaver.ast.Literal;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class DescriptionResDto {
    private String title;
    private String htmlContent;
    private String thumbnailImageUrl;
    private List<String> galleryImageUrls;

    public static DescriptionResDto of(ProductDescription description) {
        List<String> imageUrls = new LinkedList<>();

        for (ProductImages galleryImageUrl : description.getGalleryImages()) {
            imageUrls.add(galleryImageUrl.getS3URL());
        }

        return DescriptionResDto.builder()
                .thumbnailImageUrl(description.getThumbnailImageS3URL())
                .htmlContent(description.getDescription())
                .title(description.getTitle())
                .galleryImageUrls(imageUrls)
                .build();
    }
}
