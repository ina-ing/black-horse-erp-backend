package com.inaing.blackhorse_erp.common.dto;

import java.time.Instant;

public record ApiResponse<T>(
        Instant timestamp,
        int status,
        String code,
        String message,
        T data) {

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(Instant.now(), status, null, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(200, "Success", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return of(200, message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return of(201, message, data);
    }

    public static <T> ApiResponse<T> error(ErrorCode error) {
        return new ApiResponse<>(Instant.now(), error.getStatus(), error.getCode(),
                error.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode error, T data) {
        return new ApiResponse<>(Instant.now(), error.getStatus(), error.getCode(),
                error.getMessage(), data);
    }

    public static <T> ApiResponse<T> error(ErrorCode error, String message) {
        return new ApiResponse<>(Instant.now(), error.getStatus(), error.getCode(),
                message, null);
    }

    public static <T> ApiResponse<T> error(int status, String code, String message) {
        return new ApiResponse<>(Instant.now(), status, code, message, null);
    }
}
