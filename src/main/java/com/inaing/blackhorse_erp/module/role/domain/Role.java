package com.inaing.blackhorse_erp.module.role.domain;


public enum Role {
    ADMIN,
    RETAILER,
    SALES,
    WAREHOUSE,
    FACTORY;
    
    public static Role fromName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return Role.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
}
}
