package com.inaing.blackhorse_erp.module.activityLog.domain.enums;

public enum ActivityAction {

    ORDER_CREATED("Created Order", ActivityPriority.LOW),
    ORDER_UPDATED("Updated Order", ActivityPriority.MEDIUM),
    ORDER_APPROVED("Approved Order", ActivityPriority.MEDIUM),
    ORDER_PROCESSING("Started Order Processing", ActivityPriority.MEDIUM),
    ORDER_FULFILLED("Fulfilled Order", ActivityPriority.HIGH),
    ORDER_CANCELLED("Cancelled Order", ActivityPriority.HIGH),

    RETURN_CREATED("Requested Return", ActivityPriority.LOW),
    RETURN_UPDATED("Updated Return", ActivityPriority.MEDIUM),
    RETURN_STATUS_CHANGED("Changed Return Status", ActivityPriority.MEDIUM),
    RETURN_STOCK_ACCEPTED("Accepted Returned Stock", ActivityPriority.HIGH),
    RETURN_CANCELLED("Cancelled Return", ActivityPriority.HIGH),

    PRODUCTION_ORDER_CREATED("Placed Production Order", ActivityPriority.LOW),
    PRODUCTION_ORDER_ACCEPTED("Accepted Production Order", ActivityPriority.MEDIUM),

    PRODUCTION_CREATED("Recorded Production", ActivityPriority.LOW),
    PRODUCTION_UPDATED("Updated Production", ActivityPriority.MEDIUM),

    SUPPLY_CREATED("Submitted Supply", ActivityPriority.LOW),
    SUPPLY_UPDATED("Updated Supply", ActivityPriority.MEDIUM),
    SUPPLY_ACCEPTED("Accepted Supply", ActivityPriority.MEDIUM),
    SUPPLY_DISPUTED("Marked Supply Issue", ActivityPriority.HIGH),

    INVENTORY_ADJUSTED("Adjusted Inventory", ActivityPriority.HIGH),
    INVENTORY_LOW_STOCK("Low Inventory Alert", ActivityPriority.HIGH),

    PRODUCT_CREATED("Created Product", ActivityPriority.LOW),
    PRODUCT_UPDATED("Updated Product", ActivityPriority.MEDIUM),
    PRODUCT_ACTIVATED("Activated Product", ActivityPriority.MEDIUM),
    PRODUCT_DEACTIVATED("Deactivated Product", ActivityPriority.HIGH),
    PRODUCT_VARIANT_DEACTIVATED("Deactivated Colorway", ActivityPriority.HIGH),
    PRODUCT_VARIANT_SIZE_DEACTIVATED("Deactivated Size", ActivityPriority.HIGH),

    CATEGORY_CREATED("Created Category", ActivityPriority.LOW),
    CATEGORY_UPDATED("Renamed Category", ActivityPriority.MEDIUM),

    EMPLOYEE_CREATED("Created Employee", ActivityPriority.MEDIUM),
    EMPLOYEE_UPDATED("Updated Employee", ActivityPriority.MEDIUM),
    EMPLOYEE_ROLE_CHANGED("Changed Employee Role", ActivityPriority.HIGH),
    EMPLOYEE_STATUS_CHANGED("Changed Employee Status", ActivityPriority.HIGH),

    RETAILER_CREATED("Onboarded Retailer", ActivityPriority.MEDIUM),
    RETAILER_UPDATED("Updated Retailer", ActivityPriority.MEDIUM),
    RETAILER_SALESMAN_REASSIGNED("Reassigned Salesman", ActivityPriority.HIGH),

    WAREHOUSE_CREATED("Created Warehouse", ActivityPriority.LOW),
    WAREHOUSE_UPDATED("Updated Warehouse", ActivityPriority.MEDIUM),
    FACTORY_CREATED("Created Factory", ActivityPriority.LOW),
    FACTORY_UPDATED("Updated Factory", ActivityPriority.MEDIUM),

    LOGIN_SUCCEEDED("Signed In", ActivityPriority.LOW),
    LOGIN_FAILED("Failed Sign In", ActivityPriority.HIGH),
    LOGGED_OUT("Signed Out", ActivityPriority.LOW);

    private final String label;
    private final ActivityPriority defaultPriority;

    ActivityAction(String label, ActivityPriority defaultPriority) {
        this.label = label;
        this.defaultPriority = defaultPriority;
    }

    public String getLabel() {
        return label;
    }

    public ActivityPriority getDefaultPriority() {
        return defaultPriority;
    }
}
