package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MPUserInformResDto {
    private String userLoginId; // 아이디 (hong123)
    private String name; // 이름 (홍길동)
    private String email; // 이메일
    private String phone; // 휴대폰
    private String address;
    private String detailAddress;
    private String postalNumber;

    // 상단 프로필 영역 데이터
    private String userType; // "individual", "corporate", "headquarters", "branch"
    private String companyName; // 회사명
    private String parentCompany; // 소속 본사명 (지점 회원일 경우)
    private boolean isHeadquarters;


    public static MPUserInformResDto of(Members member) {
        if (member.getRole() == Role.INDIVIDUAL) {
            return MPUserInformResDto.builder()
                    .userLoginId(member.getLoginId())
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .name(member.getName())
                    .userType(member.getRole().getRole())
                    .isHeadquarters(false)
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalNumber(member.getZipCode())
                    .build();
        }



        else if (member.getRole() == Role.HEADQUARTERS) {
            return MPUserInformResDto.builder()
                    .userLoginId(member.getLoginId())
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .name(member.getName())
                    .userType(member.getRole().getRole())
                    .isHeadquarters(true)
                    .companyName(member.getName())
                    .parentCompany(null)
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalNumber(member.getZipCode())
                    .build();
        }


        else if (member.getRole() == Role.BRANCH) {
            BranchMembers m = (BranchMembers) member;
            return MPUserInformResDto.builder()
                    .userLoginId(member.getLoginId())
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .name(member.getName())
                    .userType(member.getRole().getRole())
                    .isHeadquarters(false)
                    .companyName(m.getBranchName())
                    .parentCompany(member.getName())
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalNumber(member.getZipCode())
                    .build();
        } return null;
    }
}
