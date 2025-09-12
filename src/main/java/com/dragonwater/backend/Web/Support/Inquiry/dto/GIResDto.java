package com.dragonwater.backend.Web.Support.Inquiry.dto;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GIResDto {
    private Long inquiryId;
    private String orderNumber;
    private Boolean isAnswered;
    private String answer;
    private String question;
    private String title;
    private LocalDateTime createdAt;

    public static GIResDto of(GeneralInquiries gi) {
        Orders order = gi.getOrder();
        return GIResDto.builder()
                .inquiryId(gi.getId())
                .orderNumber(order == null ?  null : order.getOrderNumber())
                .isAnswered(gi.getIsAnswered())
                .answer(gi.getAnswer())
                .question(gi.getContent())
                .title(gi.getTitle())
                .createdAt(gi.getCreatedAt())
                .build();
    }
}
