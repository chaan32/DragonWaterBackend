package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Data
@Builder
public class PreOrderResponseDto {
    private String orderId;
    private int amount;
    private String method;
    private String goodsName;
    private String clientId;

    public static PreOrderResponseDto of(Orders orders) {
        List<OrderItems> items = orders.getItems();


        String productName = items.get(0).getProduct().getName();

        if (items.size() > 1) {
            productName += "외" + (items.size() - 1) + "종";
        }



        return PreOrderResponseDto.builder()
                .orderId(orders.getOrderNumber())
//                .amount(Integer.valueOf(orders.getTotalPrice().intValue()))
                .amount(orders.getTotalPrice().intValue())
                .method(orders.getPaymentMethod().getDescription())
                .goodsName(productName)
                .clientId("R2_a62db6967356421d93f039fbfe6f8f44")
                .build();
    }
}
