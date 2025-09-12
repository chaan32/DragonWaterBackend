package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Payment.domain.PaymentMethod;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Shipment.domain.ShipmentStatus;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class OrderResDtoV2 {
    // 주문 품목
    private List<OrderItemResDtoV2> items;
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
    private String postalCode;
    private String trackingNumber;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;

    public static OrderResDtoV2 of(Orders order) {
        Shipments shipment = order.getShipment();
        Members member = order.getMember();
        int size = order.getItems().size();
        String productName = order.getItems().getFirst().getProduct().getName();
        if (size != 1) {
            productName = productName + " 외 " + (size-1) ;
        }
        List<OrderItemResDtoV2> items = new LinkedList<>();
        for (OrderItems item : order.getItems()) {
            items.add(OrderItemResDtoV2.of(item));
        }

        return OrderResDtoV2.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .ordererName(member.getName())
                .ordererType(member.getRole().getRole())
                .recipientName(shipment.getRecipientName())
                .recipientPhone(shipment.getRecipientPhone())
                .branchName(member instanceof BranchMembers ?((BranchMembers) member).getBranchName():null)
                .shippingAddress(shipment.getAddress() + " / " + shipment.getDetailAddress())
                .orderDate(order.getCreatedAt())
                .productName(productName)
                .totalAmount(order.getTotalPrice())
                .shippingStatus(shipment.getShipmentStatus().getDescription())
                .isShipmentProcessed(shipment.getShipmentStatus() != ShipmentStatus.PREPARING)
                .trackingNumber(shipment.getInvoiceNumber())
                .items(items)
                .postalCode(shipment.getPostNumber())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }
}
