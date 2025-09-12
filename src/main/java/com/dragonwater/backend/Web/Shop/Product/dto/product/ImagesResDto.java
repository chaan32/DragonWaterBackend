package com.dragonwater.backend.Web.Shop.Product.dto.product;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductImages;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class ImagesResDto {
    private String thumbnailImageS3URL;
    private ConcurrentHashMap<String, Long> galleryImagesS3URL;

    public static ImagesResDto of(ProductDescription description) {
        ConcurrentHashMap<String, Long> galleryImagesS3URL = new ConcurrentHashMap<>();
        for (ProductImages galleryImage : description.getGalleryImages()) {
            galleryImagesS3URL.put(galleryImage.getS3URL(), galleryImage.getId());
        }
        return ImagesResDto.builder()
                .thumbnailImageS3URL(description.getThumbnailImageS3URL())
                .galleryImagesS3URL(galleryImagesS3URL)
                .build();
    }
}
