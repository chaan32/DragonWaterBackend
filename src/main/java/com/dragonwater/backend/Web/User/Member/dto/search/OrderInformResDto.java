package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderInformResDto {
    private String orderNumber;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal shipmentFee;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    public static OrderInformResDto of(Orders order) {
        int size = order.getItems().size();
        BigDecimal price = BigDecimal.ZERO;
        List<OrderItems> items =
                order.getItems();
        for (OrderItems item : items) {
            price = price.add(item.getTotalPrice());
        }
        String productName = order.getItems().getFirst().getProduct().getName();
        if (size != 1) {
            productName = productName + " 외 " + (size-1) ;
        }
        return OrderInformResDto.builder()
                .orderNumber(order.getOrderNumber())
                .productName(productName)
                .shipmentFee(order.getShipmentFee())
                .productPrice(price)
                .totalPrice(price.add(order.getShipmentFee()))
                .createdAt(order.getCreatedAt())
                .build();
    }
}
