package com.dragonwater.backend.Web.Shop.Product.service.interf;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDisplay;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ConstructMainPageReqDto;

public interface ProductDisplayService {

    /**
     * 디스플레이할 아이템을 세팅하는 메소드
     *
     * @param dto 구성하는 메소드
     */
    void setDisplayItems(ConstructMainPageReqDto dto);

    /**
     * 현재 디스플레이를 담당하는 아이템을 가져오는 메소드
     *
     * @return
     */
    ProductDisplay currentMainPageProduct();
}
