package com.dragonwater.backend.Web.User.Auth.dto.Login;

import com.dragonwater.backend.Web.User.Member.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginSuccessResDto {
    private Long id; // 식별 값
    private String username; // 로그인 아이디
    private String name; // 진짜 이름
    private String email; // 이메일
    private String userType; // ROLE
    private String companyName;
    private Boolean isHeadQuarters; // 본사인지
    private String parentCompany; // 지점인 경우에 본사 명
    private String[] permissions;

    public static LoginSuccessResDto of(IndividualMembers member){ // 개인 회원 로그인 DTO
        return LoginSuccessResDto.builder()
                .id(member.getId())
                .username(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .userType(member.getRole().getRole())
                .build();
    }

    public static LoginSuccessResDto of(HeadQuarterMembers member) { // 본사 회원 로그인 DTO
        return LoginSuccessResDto.builder()
                .id(member.getId())
                .username(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .userType(member.getRole().getRole())
                .companyName(member.getName())
                .isHeadQuarters(true)
                .build();
    }

    public static LoginSuccessResDto of(BranchMembers member) { // 본사 지점 로그인 DTO
        return LoginSuccessResDto.builder()
                .id(member.getId())
                .username(member.getLoginId())
                .name(member.getBranchName())
                .email(member.getEmail())
                .userType(member.getRole().getRole())
                .companyName(member.getBranchName())
                .isHeadQuarters(false)
                .parentCompany(member.getName())
                .build();
    }

    public static LoginSuccessResDto of(AdminMembers member) { // 관리자 계정 로그인 DTO
        return LoginSuccessResDto.builder()
                .id(member.getId())
                .username(member.getLoginId())
                .name("관리자 계정")
                .email(member.getEmail())
                .userType(member.getRole().getRole())
                .companyName(null)
                .isHeadQuarters(false)
                .parentCompany(null)
                .permissions(new String[]{"MANAGE_USERS", "VIEW_DASHBOARD", "SYSTEM_CONFIG"})
                .build();
    }
}
