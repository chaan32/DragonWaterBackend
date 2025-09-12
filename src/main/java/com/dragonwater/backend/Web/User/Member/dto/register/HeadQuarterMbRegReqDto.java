package com.dragonwater.backend.Web.User.Member.dto.register;

import lombok.Data;

@Data
public class HeadQuarterMbRegReqDto {
    private String memberType;
    private String corporateType; // 단일, 본사, 지점
    private String id;
    private String password;
    private String companyName;
    private String businessNumber;
    private String businessType;

    private String email;
    private String phone;
    private String address;
    private String detailAddress;
    private String postalCode;
    private Boolean termsAccepted;
    private Boolean privacyAccepted;
}
