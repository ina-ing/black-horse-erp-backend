package com.inaing.blackhorse_erp.module.auth.dto;

public record LoginResult<T>(
        String accessToken,
        long expiresIn,
        T user) {

}
