package com.dragonwater.backend.Web.Support.Inquiry.General.dto;

import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GeneralInquiryResDto {
    private Long inquiryId;
    private String category;
    private String title;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;
    private Boolean isAnswered;
    private String question;
    private String answer;

    public static GeneralInquiryResDto of(GeneralInquiries inquiry) {
        Members member = inquiry.getMember();
        return GeneralInquiryResDto.builder()
                .inquiryId(inquiry.getId())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .userName(member.getName())
                .userEmail(member.getEmail())
                .createdAt(inquiry.getCreatedAt())
                .isAnswered(inquiry.getIsAnswered())
                .question(inquiry.getContent())
                .answer(inquiry.getAnswer())
                .build();
    }
}
