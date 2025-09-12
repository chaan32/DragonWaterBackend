package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Shipment.domain.ShipmentStatus;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class MPBranchesDto {
    private Long orderId;
    private String orderNumber;
    private String branchName;
    private LocalDateTime createdAt;

    private BigDecimal shipmentFee;
    private PaymentStatus paymentStatus;
    private ShipmentStatus shipmentStatus;
    private List<MPBranchOrderItemsDto> branchOrders;

    public static MPBranchesDto of(Orders order) {

        List<MPBranchOrderItemsDto> branchOrders = new LinkedList<>();
        for (OrderItems branchOrder : order.getItems()) {
            branchOrders.add(MPBranchOrderItemsDto.of(branchOrder));
        }

        BranchMembers member = (BranchMembers) order.getMember();

        return MPBranchesDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .branchName(member.getBranchName())
                .createdAt(order.getCreatedAt())
                .shipmentFee(order.getShipmentFee())
                .paymentStatus(order.getPaymentStatus())
                .shipmentStatus(order.getShipment().getShipmentStatus())
                .branchOrders(branchOrders)
                .build();
    }
}
