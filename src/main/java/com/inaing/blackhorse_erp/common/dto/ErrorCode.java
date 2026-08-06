package com.inaing.blackhorse_erp.common.dto;

public enum ErrorCode {
    UNAUTHENTICATED(401, "Authentication required"),
    INVALID_CREDENTIALS(401, "Invalid phone number or password"),
    INVALID_OTP(401, "Invalid OTP"),
    OTP_EXPIRED(401, "OTP has expired"),
    OTP_NOT_FOUND(404, "No OTP found for this request"),
    OTP_RESEND_TOO_SOON(429, "Please wait before requesting another OTP"),
    TOKEN_INVALID(401, "Invalid token"),
    TOKEN_EXPIRED(401, "Session expired"),
    ACCOUNT_DISABLED(403, "Account is not active"),
    ACCESS_DENIED(403, "You do not have permission to perform this action"),

    VALIDATION_FAILED(400, "Request validation failed"),
    MALFORMED_REQUEST(400, "Request body is missing or malformed"),
    MISSING_PARAMETER(400, "A required parameter is missing"),
    INVALID_ENUM_VALUE(400, "Request contains an invalid enum value"),

    NOT_FOUND(404, "Resource not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    RETAILER_NOT_FOUND(404, "Retailer not found"),
    EMPLOYEE_TYPE_NOT_FOUND(404, "Employee type not found"),
    ROLE_NOT_FOUND(404, "Role not found"),
    ORDER_NOT_FOUND(404, "Order not found"),

    ORDER_UPDATE_DENIED(403, "Access denied. You are not authorized to update this order."),
    RETURN_UPDATE_DENIED(403, "Access denied. You are not authorized to update this return."),

    DUPLICATE_PHONE(409, "Phone number already registered"),
    DUPLICATE_EMAIL(409, "Email already registered"),
    DUPLICATE_RESOURCE(409, "Resource already exists"),
    BUSINESS_RULE_VIOLATION(409, "Operation violates a business rule"),
    IDENTIFIER_ALREADY_EXISTS(409, "Generated identifier conflicts with an existing resource"),

    INTERNAL_ERROR(500, "An unexpected error occurred"),
    SERVICE_UNAVAILABLE(503, "Service temporarily unavailable");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getCode() {
        return name();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}