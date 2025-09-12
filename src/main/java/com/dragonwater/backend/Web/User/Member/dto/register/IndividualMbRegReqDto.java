package com.dragonwater.backend.Web.User.Member.dto.register;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndividualMbRegReqDto {
    private String memberType;
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String detailAddress;
    private String postalCode;
    private Boolean termsAccepted;
    private Boolean privacyAccepted;
    private Boolean marketingAccepted;
}
