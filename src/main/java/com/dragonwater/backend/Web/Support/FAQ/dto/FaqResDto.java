package com.dragonwater.backend.Web.Support.FAQ.dto;

import com.dragonwater.backend.Web.Support.FAQ.domain.FAQs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaqResDto {
    private Long id;
    private Long categoryId;
    private String question;
    private String answer;

    public static FaqResDto of(FAQs faQs) {
        return FaqResDto.builder()
                .id(faQs.getId())
                .categoryId(faQs.getCategory().getId())
                .question(faQs.getQuestion())
                .answer(faQs.getAnswer())
                .build();
    }
}
