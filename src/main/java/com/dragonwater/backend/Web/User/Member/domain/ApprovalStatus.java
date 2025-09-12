package com.dragonwater.backend.Web.User.Member.domain;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
    PENDING("심사 대기"),      // 가입 신청 후 승인을 기다리는 초기 상태
    APPROVED("승인 완료"),     // 관리자가 가입을 승인한 상태
    REJECTED("승인 거절"),     // 관리자가 가입을 거절한 상태
    SUSPENDED("이용 정지");     // 승인되었으나 이후에 이용이 정지된 상태

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }
}
