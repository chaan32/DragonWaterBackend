package com.dragonwater.backend.Web.User.Member.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MembersUpdateReqDto {
    private String name;
    private String email;
    private String phone;
    private String postalNumber;
    private String address;
    private String detailAddress;
    private String companyName;
}