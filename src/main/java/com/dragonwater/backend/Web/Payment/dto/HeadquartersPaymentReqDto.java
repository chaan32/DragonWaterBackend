package com.dragonwater.backend.Web.Payment.dto;

import lombok.Data;

import java.util.List;

@Data
public class HeadquartersPaymentReqDto {
    private List<Long> orderId;
    private String paymethod;
}
