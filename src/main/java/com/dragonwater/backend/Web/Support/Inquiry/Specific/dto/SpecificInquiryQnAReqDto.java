package com.dragonwater.backend.Web.Support.Inquiry.Specific.dto;

import lombok.Data;

@Data
public class SpecificInquiryQnAReqDto {
    private Long userId;
    private Long productId;
    private String question;
}
