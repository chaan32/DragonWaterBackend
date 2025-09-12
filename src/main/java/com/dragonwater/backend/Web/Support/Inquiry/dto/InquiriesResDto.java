package com.dragonwater.backend.Web.Support.Inquiry.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class InquiriesResDto {
    private List<GIResDto> generalInquiries;
    private List<SIResDto> productInquiries;
    private List<ClaimResDto> claims;

    public static InquiriesResDto of(List<GIResDto> gi, List<SIResDto> si, List<ClaimResDto> cl) {
        return InquiriesResDto.builder()
                .generalInquiries(gi)
                .productInquiries(si)
                .claims(cl)
                .build();
    }
}
