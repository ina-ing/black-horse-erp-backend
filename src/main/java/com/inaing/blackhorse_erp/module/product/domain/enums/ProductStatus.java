package com.inaing.blackhorse_erp.module.product.domain.enums;

public enum ProductStatus {
    DRAFT, ACTIVE, INACTIVE;

    public static ProductStatus fromName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return ProductStatus.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}