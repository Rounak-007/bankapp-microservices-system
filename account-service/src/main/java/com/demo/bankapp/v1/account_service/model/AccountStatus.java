package com.demo.bankapp.v1.account_service.model;

public enum AccountStatus {
    ACTIVE,
    BLOCKED,
    CLOSED;

    // valid transitions for each state
    public boolean canTransitionTo(AccountStatus target) {
        return switch (this) {
            case ACTIVE -> target == BLOCKED || target == CLOSED;
            case BLOCKED -> target == ACTIVE || target == CLOSED;
            case CLOSED -> false; // A closed account can NEVER transition to anything
        };
    }
}
