package com.dragonwater.backend.Web.Shipment.domain;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Shipment.dto.ShipmentReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.Order;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Shipments {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 주문과 배송 정보의 1:1 맵핑
    @OneToOne(mappedBy = "shipment")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Orders order;



    // 배송 정보
    @Column(name = "invoice_number", length = 30)
    private String InvoiceNumber; // 송장 번호
    @Column(name = "recipient_name", length = 15)
    private String recipientName;
    @Column(name = "recipient_phone", length = 13)
    private String recipientPhone;
    @Column(name = "post_number", length = 5)
    private String postNumber;
    @Column(name = "address", length = 40)
    private String address;
    @Column(name = "detail_address", length = 10)
    private String detailAddress;

    @Column(name = "shipment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

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


    public static Shipments of(ShipmentReqDto dto) {
        return Shipments.builder()
                .recipientName(dto.getRecipientName())
                .recipientPhone(dto.getRecipientPhone())
                .postNumber(dto.getPostNumber())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .shipmentStatus(ShipmentStatus.PREPARING)
                .build();
    }

    public void processShipment(String invoiceNumber) {
        this.InvoiceNumber = invoiceNumber;
        this.shipmentStatus = ShipmentStatus.SHIPPED;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
