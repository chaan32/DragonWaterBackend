package com.dragonwater.backend.Web.User.Member.domain;

import com.dragonwater.backend.Web.User.Member.dto.register.CorporateMbRegReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CORPORATE")
@SuperBuilder
public class CorporateMembers extends Members{

    @Column(name = "business_type", length = 40)
    private String businessType; // 법인의 종류

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    // 사업자등록증 S3에 저장한거
    private String bizRegUrl;

    // 회원 등록 승인 상태
    private ApprovalStatus approvalStatus;

    public static CorporateMembers of(CorporateMbRegReqDto dto,String s3Url, String encodedPassword) {
        return CorporateMembers.builder()
                .loginId(dto.getId())
                .loginPw(encodedPassword)
                .name(dto.getCompanyName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .role(Role.CORPORATE) // 역할 설정
                .bizRegUrl(s3Url)
                .approvalStatus(ApprovalStatus.PENDING)

                .businessType(dto.getBusinessType())
                .businessRegistrationNumber(dto.getBusinessNumber())
                .build();
    }
}
