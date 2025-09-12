package com.dragonwater.backend.Web.Payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HQPaymentDto {
    private String orderNumber;
    private int totalPrice;

    public static HQPaymentDto of(String orderNumber, int totalPrice) {
        return HQPaymentDto.builder()
                .orderNumber(orderNumber)
                .totalPrice(totalPrice)
                .build();
    }
}
