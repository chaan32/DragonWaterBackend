package com.dragonwater.backend.Web.Order.domain;


import com.dragonwater.backend.Web.Order.dto.OrderReqDto;
import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.Payment.domain.PaymentMethod;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // id만 사용
@ToString(onlyExplicitlyIncluded = true)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, name = "order_number", nullable = false)
    private String orderNumber;

    // ------------------------------------------- Mapping 정보 ----------------------------------
    // 주문자 N:1로 연결 됨
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    // 배송 정보 1:1로 연결 됨
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Shipments shipment;

    // 주문 품목과 1:N으로 연결
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> items;

    // 환불 정보 -> null 가능
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_id")
    private Claims claim;


    // 한번에 결제하는 거
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "batch_payment_id")
    private BatchPayment batchPayment;

    // ------------------------------------------- Mapping 정보 ----------------------------------


    // 결제 금액 정보
    @Column(name = "total_price", nullable = false, precision = 11, scale = 0)
    private BigDecimal totalPrice;
    @Column(name = "product_price", nullable = false, precision = 11, scale = 0)
    private BigDecimal productPrice;
    @Column(name = "coupon_discount_price", nullable = false, precision = 11, scale = 0)
    private BigDecimal couponDiscountPrice;
    @Column(name = "point_discount_price", nullable = false, precision = 11, scale = 0)
    private BigDecimal pointDiscountPrice;
    @Column(name = "shipment_fee", nullable = false, precision = 6, scale = 0)
    private BigDecimal shipmentFee;

    // 결제 정보
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

    public void setShipment(Shipments shipment) {
        this.shipment = shipment;
        shipment.setOrder(this);
    }
    public static Orders of(OrderReqDto dto, Members member, String orderNumber) {
        return Orders.builder()
                .orderNumber(orderNumber)
                .member(member)
                .totalPrice(dto.getTotalPrice())
                .productPrice(dto.getProductPrice())
                .couponDiscountPrice(dto.getCouponDiscountPrice())
                .pointDiscountPrice(dto.getPointDiscountPrice())
                .shipmentFee(dto.getShipmentFee())
                .paymentMethod(PaymentMethod.fromText(dto.getPaymentMethod()))
                .paymentStatus(member.getRole()== Role.BRANCH?PaymentStatus.UNPAID:PaymentStatus.PENDING)
                .build();
    }

    public void pay() {
        this.paymentStatus = PaymentStatus.APPROVED;
    }

    public void subStocks() {
        List<OrderItems> items = this.getItems();
        for (OrderItems item : items) {

        }
    }


    public void setBatchPayment(BatchPayment batchPayment) {
        this.batchPayment = batchPayment;
    }
}
