package com.dragonwater.backend.Web.Support.FAQ.dto;

import com.dragonwater.backend.Web.Support.FAQ.domain.FaqCategories;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaqCategoryResDto {
    private Long id;
    private String name;

    public static FaqCategoryResDto of(FaqCategories category) {
        return FaqCategoryResDto.builder()
                .id(category.getId())
                .name(category.getCategory())
                .build();
    }
}
