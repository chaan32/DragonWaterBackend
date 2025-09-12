package com.dragonwater.backend.Web.Order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_sequence")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSequence {

    // "YYYY-MM" 형식을 기본 키(PK)로 사용합니다. (예: "2025-08")
    @Id
    @Column(name = "period")
    private String yearMonth;

    // 해당 월의 마지막으로 생성된 시퀀스 번호를 저장합니다.
    @Column(name = "order_number")
    private Long orderSeq;

    public OrderSequence(String yearMonth) {
        this.yearMonth = yearMonth;
        this.orderSeq = 1L; // 해당 월의 첫 시퀀스는 1로 시작합니다.
    }

    // 다음 번호를 위해 내부 카운트를 1 증가시킵니다.
    public Long increaseAndGetSequence() {
        this.orderSeq++;
        return this.orderSeq;
    }
}
