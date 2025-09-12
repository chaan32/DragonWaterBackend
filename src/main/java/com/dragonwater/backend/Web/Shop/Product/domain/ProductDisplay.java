package com.dragonwater.backend.Web.Shop.Product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long newProductId;
    private Long bestProductId;
    private Long recommendationProductId;

    public static ProductDisplay of(Long newProductId, Long bestProductId, Long recommendationProductId) {
        return ProductDisplay.builder()
                .bestProductId(bestProductId)
                .newProductId(newProductId)
                .recommendationProductId(recommendationProductId)
                .build();
    }
}
