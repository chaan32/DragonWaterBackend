package com.dragonwater.backend.Web.User.Member.dto.register;

import lombok.Data;

@Data
public class BranchMbRegReqDto {
    private String memberType;
    private String corporateType; // 단일, 본사, 지점
    private String id;
    private String password;
    private String email;

    private Long headquartersId;
    private String headquartersName;
    private String branchName;
    private String companyName;
    private String businessNumber;
    private String businessType;

    private String phone;
    private String address;
    private String detailAddress;
    private String postalCode;

    // 관리자의 정보를 추가로 받는 form
    private String managerName;
    private String managerPhone;

    private Boolean termsAccepted;
    private Boolean privacyAccepted;


}
