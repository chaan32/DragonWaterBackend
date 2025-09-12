package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CorporatesResDto {
    private Long id;
    private String companyName;
    private String branchName;
    private String email;
    private String phone;
    private String businessRegistrationNumber;
    private String businessType;
    private Boolean isHeadquarters;
    private LocalDateTime joinDate;
    private Integer orderCount;
    private Long totalAmount;
    private String managerName;
    private String managerPhone;

    public static CorporatesResDto of(BranchMembers members) {
        return CorporatesResDto.builder()
                .id(members.getId())
                .companyName(members.getName())
                .branchName(members.getBranchName())
                .email(members.getEmail())
                .phone(members.getPhone())
                .businessRegistrationNumber(members.getBusinessRegistrationNumber())
                .businessType(members.getBusinessType())
                .isHeadquarters(false)
                .joinDate(members.getCreatedAt())
                .orderCount(members.getOrders().size())
                .totalAmount(12345L)
                .build();
    }
    public static CorporatesResDto of(HeadQuarterMembers members) {
        return CorporatesResDto.builder()
                .id(members.getId())
                .companyName(members.getName())
                .email(members.getEmail())
                .phone(members.getPhone())
                .businessRegistrationNumber(members.getBusinessRegistrationNumber())
                .businessType(members.getBusinessType())
                .isHeadquarters(true)
                .joinDate(members.getCreatedAt())
                .orderCount(null)
                .totalAmount(12345L)
                .build();
    }
}
