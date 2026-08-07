package com.inaing.blackhorse_erp.module.returns.domain.enums;

public enum ReturnReason {
    STOCK, DAMAGE, REPAIR;

    public static ReturnReason fromName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return ReturnReason.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
