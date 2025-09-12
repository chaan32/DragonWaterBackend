package com.dragonwater.backend.Web.Shop.Product.service.implement;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.ProductNotFoundException;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.Shop.Product.repository.DescriptionRepository;
import com.dragonwater.backend.Web.Shop.Product.repository.ProductRepository;
import com.dragonwater.backend.Web.Shop.Product.service.interf.DescriptionService;
import com.dragonwater.backend.Web.User.Member.dto.product.DescriptionResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DescriptionServiceImpl implements DescriptionService {
    private final CloudStorageService cloudStorageService;

    private final DescriptionRepository descriptionRepository;
    private final ProductRepository productRepository;


    public DescriptionResDto getContent(ProductDescription description) {
        return DescriptionResDto.of(description);
    }

    public void updateDescription(ProductDescription existingDescription, ProductDescriptionDto dto) {
        Long productId = dto.getProductId();

        Products product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        // 기존 설명 엔티티를 업데이트
        existingDescription.setTitle(dto.getTitle());
        existingDescription.setDescription(dto.getHtmlContents());

        // 새 썸네일이 있으면 업로드하고 URL 업데이트
        if (dto.getThumbnailImage() != null && !dto.getThumbnailImage().isEmpty()) {
            String newThumbnailUrl = cloudStorageService.uploadProductThumbnailImage(dto.getThumbnailImage(), product);
            existingDescription.setThumbnailImageS3URL(newThumbnailUrl);
        } else if (dto.getThumbnailImageUrl() != null) {
            // 기존 URL 유지
            existingDescription.setThumbnailImageS3URL(dto.getThumbnailImageUrl());
        }

        // 갤러리 이미지 처리 (필요하면)
        if (dto.getGalleryImages() != null && !dto.getGalleryImages().isEmpty()) {
            // 갤러리 이미지 업데이트 로직
        }

        // 저장
        descriptionRepository.save(existingDescription);
    }
}
