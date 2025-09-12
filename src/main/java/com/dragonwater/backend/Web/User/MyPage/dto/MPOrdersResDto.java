package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Shipment.domain.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class MPOrdersResDto {
    private Long orderId;
    private String orderName;
    private LocalDateTime createdAt;
    private BigDecimal price;
    private BigDecimal shipmentFee;
    private ShipmentStatus shipmentStatus;
    private PaymentStatus paymentStatus;
    private String trackingNumber;
    private String products;
    private List<OrderItemResDto> items;

    public static MPOrdersResDto of(Orders order) {

        List<OrderItems> items1 = order.getItems();
        List<OrderItemResDto> ite = new LinkedList<>();
        for (OrderItems orderItems : items1) {
            ite.add(OrderItemResDto.of(orderItems));
        }
        String pn = items1.getFirst().getProduct().getName();
        if (items1.size() != 1) {
            pn = pn + "외 "+(items1.size()-1) +"종";
        }
        return MPOrdersResDto.builder()
                .orderId(order.getId())
                .orderName(order.getOrderNumber())
                .createdAt(order.getCreatedAt())
                .price(order.getTotalPrice())
                .shipmentStatus(order.getShipment().getShipmentStatus())
                .paymentStatus(order.getPaymentStatus())
                .trackingNumber(order.getShipment().getInvoiceNumber())
                .items(ite)
                .products(pn)
                .shipmentFee(order.getShipmentFee())
                .build();
    }
}
