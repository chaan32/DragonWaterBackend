package com.dragonwater.backend.Web.Payment.domain;

public enum PaymentMethod {
    CARD("신용카드"),               // Enum 상수(설명)
    BANK_TRANSFER("계좌이체"),
    CORPORATE_PAYMENT("법인결제");

    private final String description; // 설명을 저장할 필드

    // 생성자
    PaymentMethod(String description) {
        this.description = description;
    }

    // 설명을 반환하는 Getter
    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("결제 수단 문자열은 비어있을 수 없습니다.");
        }

        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(text)) {
                return method;
            }
        }

        // 일치하는 상수가 없을 경우 예외를 발생시켜 잘못된 값이 들어왔음을 명확히 알립니다.
        throw new IllegalArgumentException("알 수 없는 결제 수단입니다: " + text);
    }
}
