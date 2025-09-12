package com.dragonwater.backend.Web.User.Member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("admin"),
    HEADQUARTERS("headquarters"),
    BRANCH("branch"),
    INDIVIDUAL("individual"),
    CORPORATE("corporate");

    private final String role;

    Role (String role) {
        this.role = role;
    }
}
