package com.dragonwater.backend.Web.Support.Claim.domain;

public enum ClaimStatus {

    RECEIVED("접수"),          // 클레임이 처음 접수된 상태
    PROCESSING("처리 중"),     // 담당자가 클레임을 확인하고 처리하는 중인 상태
    COMPLETED("처리 완료"),    // 교환 또는 환불 처리가 모두 완료된 상태
    REJECTED("반려");          // 클레임 요청이 거부된 상태

    private final String description;

    ClaimStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}