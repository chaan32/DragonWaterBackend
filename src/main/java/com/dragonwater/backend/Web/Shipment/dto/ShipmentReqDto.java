package com.dragonwater.backend.Web.Shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShipmentReqDto {
    // 받는 사람 이름, 연락처
    private String recipientName;
    private String recipientPhone;

    private String postNumber;
    private String address;
    private String detailAddress;
    private String memo;
}
