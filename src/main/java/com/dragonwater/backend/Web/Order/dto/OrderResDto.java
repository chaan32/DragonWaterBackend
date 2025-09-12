package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Shipment.domain.ShipmentStatus;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResDto {
    private Long id;
    private String orderNumber;
    private String ordererName;
    private String ordererType;
    private String recipientName;
    private String branchName;
    private String recipientPhone;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private String daysSinceOrder;
    private String productName;
    private BigDecimal totalAmount;
    private String shippingStatus;
    private Boolean isShipmentProcessed;
    private String trackingNumber;

    public static OrderResDto of(Orders order) {
        Shipments shipment = order.getShipment();
        Members member = order.getMember();
        int size = order.getItems().size();
        String productName = order.getItems().getFirst().getProduct().getName();
        if (size != 1) {
            productName = productName + " 외 " + (size-1) ;
        }

        return OrderResDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .ordererName(member.getName())
                .ordererType(member.getRole().getRole())
                .recipientName(shipment.getRecipientName())
                .recipientPhone(shipment.getRecipientPhone())
                .branchName(member instanceof BranchMembers?((BranchMembers) member).getBranchName():null)
                .shippingAddress(shipment.getAddress() + " / " + shipment.getDetailAddress())
                .orderDate(order.getCreatedAt())
                .productName(productName)
                .totalAmount(order.getTotalPrice())
                .shippingStatus(shipment.getShipmentStatus().getDescription())
                .isShipmentProcessed(shipment.getShipmentStatus() != ShipmentStatus.PREPARING)
                .trackingNumber(shipment.getInvoiceNumber())
                .build();
    }
}
