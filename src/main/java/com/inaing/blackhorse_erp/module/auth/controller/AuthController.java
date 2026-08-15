package com.inaing.blackhorse_erp.module.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.auth.dto.LoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.LoginUserDto;
import com.inaing.blackhorse_erp.module.auth.usecase.IAuthUseCase;
import com.inaing.blackhorse_erp.security.jwt.AccessTokenCookie;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginUserDto>> login(
            @Valid @RequestBody LoginRequestDto request) {
        LoginResult<LoginUserDto> result = authUseCase.login(request);
        ResponseCookie cookie = AccessTokenCookie.issue(result.accessToken(), result.expiresIn());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.ok("Login Successful", result.user()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        ResponseCookie cookie = AccessTokenCookie.clear();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.ok("Logout Successful", null));
    }

}
