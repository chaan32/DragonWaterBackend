package com.dragonwater.backend.Web.Shop.Product.dto.description;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDescriptionDto {
    private Long productId;
    private String title;
    private String htmlContents;
    private MultipartFile thumbnailImage;
    private String thumbnailImageUrl; // 기존 썸네일 URL을 위한 필드 추가
    private List<MultipartFile> galleryImages;

    public static ProductDescriptionDto of(
            Long productId,
            String title,
            String htmlContents,
            MultipartFile thumbnailImage,
            List<MultipartFile> galleryImages) {
        return ProductDescriptionDto.builder()
                .productId(productId)
                .title(title)
                .htmlContents(htmlContents)
                .thumbnailImage(thumbnailImage)
                .galleryImages(galleryImages)
                .build();
    }

    public static ProductDescriptionDto ofWithExistingThumbnail(
            Long productId,
            String title,
            String htmlContents, // htmlContent -> htmlContents로 맞춤
            String existingThumbnailUrl,
            List<MultipartFile> galleryImages) {

        return ProductDescriptionDto.builder()
                .productId(productId)
                .title(title)
                .htmlContents(htmlContents)
                .thumbnailImageUrl(existingThumbnailUrl) // 기존 URL 설정
                .galleryImages(galleryImages)
                .build();
    }
}