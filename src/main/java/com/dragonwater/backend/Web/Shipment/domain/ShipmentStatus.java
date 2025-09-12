package com.dragonwater.backend.Web.Shipment.domain;

import lombok.Getter;
 @Getter
public enum ShipmentStatus {
     PREPARING("preparing"),
     SHIPPED("shipped"),
     DELIVERED("delivered");

    private final String description;

     ShipmentStatus(String description) {
        this.description = description;
    }
}

