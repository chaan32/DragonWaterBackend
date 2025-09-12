package com.dragonwater.backend.Web.User.Member.domain;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("ADMIN")
@SuperBuilder
public class AdminMembers extends Members{

    public static AdminMembers of(String loginId, String loginPw) {
        return AdminMembers.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .name("관리자")
                .email("ttgy61@naver.com")
                .phone("01046587418")
                .address("서울")
                .detailAddress("어딘가")
                .role(Role.ADMIN) // 역할 설정
                .build();
    }

}
