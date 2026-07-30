package com.inaing.blackhorse_erp.security.jwt;

import org.springframework.http.ResponseCookie;

public class AccessTokenCookie {
    public static final String NAME = "access_token";

    public static ResponseCookie issue(String token, long maxAgeSeconds) {
        return base(token)
                .maxAge(maxAgeSeconds)
                .build();
    }

    public static ResponseCookie clear() {
        return base("")
                .maxAge(0)
                .build();
    }

    private static ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(NAME, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax");
    }
}
