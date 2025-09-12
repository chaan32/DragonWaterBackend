package com.dragonwater.backend.Web.Support.Inquiry.dto;

import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Builder
@Getter
public class SIResDto {
    private Long inquiryId;
    private Boolean isAnswered;
    private String answer;
    private String question;
    private LocalDateTime createdAt;

    public static SIResDto of(ProductsInquiries pi) {
        return SIResDto.builder()
                .inquiryId(pi.getId())
                .isAnswered(pi.getAnswered())
                .answer(pi.getAnswer())
                .question(pi.getQuestion())
                .createdAt(pi.getCreatedAt())
                .build();
    }
}
