package com.dragonwater.backend.Web.Shop.Product.service.interf;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.User.Member.dto.product.DescriptionResDto;

public interface DescriptionService {

    /**
     * 제품의 설명을 가져오는 가져오는 메소드
     * @param description Description 객체를 그대로
     * @return
     */
    DescriptionResDto getContent(ProductDescription description);

    /**
     * 재퓸의 설명을 업데이트 하는 메소드
     * @param existingDescription 제품 설명 객체
     * @param dto 제품의 수정 내용을 담은 DTO
     */
    void updateDescription(ProductDescription existingDescription, ProductDescriptionDto dto);
}
