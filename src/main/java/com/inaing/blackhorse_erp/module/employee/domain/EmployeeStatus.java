package com.inaing.blackhorse_erp.module.employee.domain;

public enum EmployeeStatus {
    ACTIVE,
    SUSPENDED,
    TERMINATED;

    public static EmployeeStatus fromName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return EmployeeStatus.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
