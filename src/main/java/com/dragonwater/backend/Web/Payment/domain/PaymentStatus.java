package com.dragonwater.backend.Web.Payment.domain;

public enum PaymentStatus {
    REFUNDED("환불완료"),
    APPROVED("결제승인"),      // 결제가 성공적으로 완료된 상태
    PENDING("결제대기"),       // 결제를 시도했으나 아직 완료되지 않은 상태 -> 계좌이체 등
    UNPAID("미결제");           // 법인 미결제

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
