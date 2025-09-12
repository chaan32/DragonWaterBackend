package com.dragonwater.backend.Web.Support.FAQ.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaqReqDto {
    private Long categoryId;
    private String question;
    private String answer;
}
