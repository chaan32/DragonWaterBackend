package com.dragonwater.backend.Web.User.Member.domain;


import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import com.dragonwater.backend.Web.User.Member.dto.register.HeadQuarterMbRegReqDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("HEADQUARTER")
@SuperBuilder
public class HeadQuarterMembers extends Members{
    @Column(name = "business_type", length = 40)
    private String businessType; // 법인의 종류

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    // 사업자등록증 S3에 저장한거
    private String bizRegUrl;

    // 회원 가입 승인 단계
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    // 본사는 여러 지점을 가짐 (1:N)
    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "headQuarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BranchMembers> branches = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "headQuarterMembers", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BatchPayment> batchPayments;

    public void addBranch(BranchMembers branch){
        this.branches.add(branch);
    }



    public static HeadQuarterMembers of(HeadQuarterMbRegReqDto dto,String s3Url, String encodedPassword) {
        return HeadQuarterMembers.builder()
                .loginId(dto.getId())
                .loginPw(encodedPassword)
                .name(dto.getCompanyName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .role(Role.HEADQUARTERS) // 역할 설정
                .bizRegUrl(s3Url)
                .approvalStatus(ApprovalStatus.PENDING)
                .zipCode(dto.getPostalCode())
                .businessType(dto.getBusinessType())
                .businessRegistrationNumber(dto.getBusinessNumber())
                .build();
    }

    public boolean approveStatus() {
        this.approvalStatus = ApprovalStatus.APPROVED;
        return true;
    }
    public boolean rejectStatus() {
        this.approvalStatus = ApprovalStatus.REJECTED;
        return true;
    }
}
