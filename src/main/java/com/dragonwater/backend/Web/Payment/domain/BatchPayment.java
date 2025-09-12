package com.dragonwater.backend.Web.Payment.domain;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BatchPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "headquarters_id", nullable = false)
    private HeadQuarterMembers headQuarterMembers;


    @ToString.Exclude
    @OneToMany(mappedBy = "batchPayment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> batchOrders;


    // 주문 번호
    @Column(name = "orderNumber", nullable = false)
    private String orderNumber;

    // 최종 결제 금액
    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public static BatchPayment of(HeadQuarterMembers headQuarterMembers,
                                  List<Orders> batchOrders,
                                  String orderNumber,
                                  BigDecimal totalPrice,
                                  PaymentMethod paymentMethod) {
        return BatchPayment.builder()
                .headQuarterMembers(headQuarterMembers)
                .batchOrders(batchOrders)
                .orderNumber(orderNumber)
                .totalPrice(totalPrice)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    public void pay() {
        this.paymentStatus = PaymentStatus.APPROVED;
    }
}
