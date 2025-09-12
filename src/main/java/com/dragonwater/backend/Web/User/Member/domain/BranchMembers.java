package com.dragonwater.backend.Web.User.Member.domain;
import com.dragonwater.backend.Web.User.Member.dto.register.BranchMbRegReqDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BRANCH")
@SuperBuilder
@ToString(exclude = "headQuarter")
public class BranchMembers extends Members{
    @Column(name = "business_type")
    private String businessType; // 법인 종류 (카페 법인 등)

    @Column(name = "branch_name")
    private String branchName; // 지점 명

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber; // 본사의 사업자 등록 번호

    // 사업자등록증 S3에 저장한거 - > 받지 말고 가져오자 이미 등록된 본사의 사업자 등록증을 가져와서 채우기
    private String bizRegUrl;


    // 추가 항목
    @Column(name = "manager_name")
    private String managerName;
    @Column(name = "manager_email")
    private String managerPhone;


    // 회원 등록 승인 상태
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private HeadQuarterMembers headQuarter;

    public static BranchMembers of(BranchMbRegReqDto dto,String s3Url, String encodedPassword, HeadQuarterMembers headQuarter) {
        return BranchMembers.builder()
                .loginId(dto.getId())
                .loginPw(encodedPassword)
                .name(headQuarter.getName())
                .email(dto.getEmail())
                .phone(dto.getManagerPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .role(Role.BRANCH) // 역할 설정
                .bizRegUrl(s3Url)
                .approvalStatus(ApprovalStatus.PENDING)
                .zipCode(dto.getPostalCode())
                .headQuarter(headQuarter)
                .businessType(dto.getBusinessType())
                .branchName(dto.getBranchName())
                .businessRegistrationNumber(dto.getBusinessNumber())
                .managerName(dto.getManagerName())
                .managerPhone(dto.getManagerPhone())
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
