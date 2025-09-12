package com.dragonwater.backend.Web.Support.Inquiry.Specific.dto;

import lombok.Data;

@Data
public class AdminAnswerReqDto {
    private Long inquiryId;
    private String answer;
}
