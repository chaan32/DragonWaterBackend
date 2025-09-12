package com.dragonwater.backend.Web.User.Member.dto.corporate;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Builder
public class ApproveResponseDto {
    private Long id;
    private String companyName;
    private String branchName;
    private String phone;
    private String email;
    private String businessRegistrationNumber;
    private String businessRegistrationURL;
    private String businessType;
    private String approvalStatus;
    private Boolean isHeadquarters;
    private String managerName;
    private String managerPhone;
    private LocalDateTime requestDate;

    public static ApproveResponseDto of(HeadQuarterMembers hqMember) {
        return ApproveResponseDto.builder()
                .id(hqMember.getId())
                .companyName(hqMember.getName())
                .phone(hqMember.getPhone())
                .email(hqMember.getEmail())
                .businessRegistrationNumber(hqMember.getBusinessRegistrationNumber())
                .businessRegistrationURL(hqMember.getBizRegUrl())
                .businessType(hqMember.getBusinessType())
                .approvalStatus(hqMember.getApprovalStatus().toString())
                .isHeadquarters(true)
                .requestDate(hqMember.getCreatedAt())
                .build();
    }

    public static ApproveResponseDto of(BranchMembers brMember) {
        return ApproveResponseDto.builder()
                .id(brMember.getId())
                .companyName(brMember.getName())
                .branchName(brMember.getBranchName())
                .phone(brMember.getPhone())
                .email(brMember.getEmail())
                .businessRegistrationNumber(brMember.getBusinessRegistrationNumber())
                .businessRegistrationURL(brMember.getBizRegUrl())
                .businessType(brMember.getBusinessType())
                .approvalStatus(brMember.getApprovalStatus().toString())
                .isHeadquarters(false)
                .requestDate(brMember.getCreatedAt())
                .managerName(brMember.getManagerName())
                .managerPhone(brMember.getManagerPhone())
                .build();
    }
}
