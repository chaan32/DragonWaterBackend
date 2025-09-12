package com.dragonwater.backend.Web.Support.Inquiry.General.dto;

import lombok.Data;

@Data
public class GeneralInquiryReqDto {
    private String category;
    private String title;
    private String content;
    private Long orderId;
}
