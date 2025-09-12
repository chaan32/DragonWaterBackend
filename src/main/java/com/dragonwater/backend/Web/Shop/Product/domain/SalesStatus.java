package com.dragonwater.backend.Web.Shop.Product.domain;

import lombok.Data;
import lombok.Getter;

@Getter
public enum SalesStatus {
    ON_SALE("판매 중"),
    SOLD_OUT("품절"),
    DISCONTINUED("판매 중단");

    private final String description;

    SalesStatus(String description) {
        this.description = description;
    }
}
