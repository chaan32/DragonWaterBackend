package com.dragonwater.backend.Web.Support.Inquiry.Specific.dto;

import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SpecificInquiryQnAResDto {
    private Long id;
    private String question;
    private Boolean isAnswered;
    private String answer;
    private LocalDateTime createdAt;

    public static SpecificInquiryQnAResDto of(ProductsInquiries inquiry) {
        return SpecificInquiryQnAResDto.builder()
                .id(inquiry.getId())
                .question(inquiry.getQuestion())
                .answer(inquiry.getAnswer())
                .isAnswered(inquiry.getAnswered())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
