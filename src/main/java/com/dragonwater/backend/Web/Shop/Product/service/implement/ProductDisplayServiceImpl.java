package com.dragonwater.backend.Web.Shop.Product.service.implement;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDisplay;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ConstructMainPageReqDto;
import com.dragonwater.backend.Web.Shop.Product.repository.DisplayRepository;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductDisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDisplayServiceImpl implements ProductDisplayService {
    private final DisplayRepository displayRepository;

    @Override
    @Transactional
    public void setDisplayItems(ConstructMainPageReqDto dto) {
        Optional<ProductDisplay> display = displayRepository.findFirstByOrderByIdAsc();
        if (display.isEmpty()) {
            displayRepository.save(
                    ProductDisplay.of(dto.getNewProducts(), dto.getBestProducts(), dto.getRecommendedProducts()
                    ));
        }
        else {
            ProductDisplay productDisplay = display.get();
            productDisplay.setBestProductId(dto.getBestProducts());
            productDisplay.setNewProductId(dto.getNewProducts());
            productDisplay.setRecommendationProductId(dto.getRecommendedProducts());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ProductDisplay currentMainPageProduct() {
        return displayRepository.findFirstByOrderByIdAsc().get();
    }
}
