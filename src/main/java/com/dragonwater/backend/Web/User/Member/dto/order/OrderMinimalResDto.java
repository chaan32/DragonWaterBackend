package com.dragonwater.backend.Web.User.Member.dto.order;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Payment.domain.PaymentMethod;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Shipment.domain.ShipmentStatus;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class OrderMinimalResDto {
    private Long id;
    private String productName;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private BigDecimal productPrice;
    private BigDecimal shipmentFee;
    private ShipmentStatus shipmentStatus;

    public static OrderMinimalResDto of(Orders order) {
        List<OrderItems> items = order.getItems();
        Shipments shipment = order.getShipment();


        BigDecimal ta = BigDecimal.ZERO;

        for (OrderItems item : items) {
            ta = ta.add(item.getTotalPrice());
        }

        String productName = items.getFirst().getProduct().getName();
        if (items.size() != 1) {
            productName += "외 "+(items.size()-1) +"종";
        }
        return OrderMinimalResDto.builder()
                .id(order.getId())
                .productName(productName)
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .productPrice(ta)
                .shipmentFee(order.getShipmentFee())
                .shipmentStatus(shipment.getShipmentStatus())
                .build();
    }
}
