package com.dragonwater.backend.Web.Shop.Product.dto.product;

import lombok.Data;

@Data
public class ChangeThumbnailImageReqDto {
    private String currentThumbnailUrl;
    private String newThumbnailUrl;
    private Long newThumbnailId;
}
