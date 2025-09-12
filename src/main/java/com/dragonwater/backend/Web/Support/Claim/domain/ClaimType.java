package com.dragonwater.backend.Web.Support.Claim.domain;

public enum ClaimType {

    EXCHANGE("exchange"),
    REFUND("refund");

    private final String description;

    ClaimType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
