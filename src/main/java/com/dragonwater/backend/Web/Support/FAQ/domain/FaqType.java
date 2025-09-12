package com.dragonwater.backend.Web.Support.FAQ.domain;

public enum FaqType {
    PRODUCT("제품"),
    ORDER("주문/배송"),
    CLAIM("교환/환불");
    private final String description;

    FaqType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
