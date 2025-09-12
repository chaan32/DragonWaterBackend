package com.dragonwater.backend.Web.User.Auth.dto.Approve;

import com.dragonwater.backend.Web.User.Member.domain.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveResponseDto {
    private Long id; // 식별 값
    private String companyName;
    private String branchName;
    private String phone;
    private String email;
    private String businessRegistrationNumber;
    private String businessRegistrationURL;
    private String businessType;
    private Boolean isHeadquarters;
    private ApprovalStatus approvalStatus;
    private Date requestDate;

}
