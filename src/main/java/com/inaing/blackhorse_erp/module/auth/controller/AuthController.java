package com.inaing.blackhorse_erp.module.auth.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.employee.EmployeeLoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.employee.EmployeeLoginResponseDto;
import com.inaing.blackhorse_erp.module.auth.dto.retailer.RetailerLoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.retailer.RetailerLoginResponseDto;
import com.inaing.blackhorse_erp.module.auth.usecase.IAuthUseCase;
import com.inaing.blackhorse_erp.security.jwt.AccessTokenCookie;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthUseCase<EmployeeLoginRequestDto, EmployeeLoginResponseDto> employeeAuthUsecase;

    private final IAuthUseCase<RetailerLoginRequestDto, RetailerLoginResponseDto> retailerAuthUseCase;

    public AuthController(
            @Qualifier("employeeAuthUseCase") IAuthUseCase<EmployeeLoginRequestDto, EmployeeLoginResponseDto> emAuthUseCase,
            @Qualifier("retailerAuthUseCase") IAuthUseCase<RetailerLoginRequestDto, RetailerLoginResponseDto> reAuthUseCase) {
        this.employeeAuthUsecase = emAuthUseCase;
        this.retailerAuthUseCase = reAuthUseCase;
    }

    @PostMapping("/e/login")
    public ResponseEntity<ApiResponse<EmployeeLoginResponseDto>> employeeLogin(
            @Valid @RequestBody EmployeeLoginRequestDto request) {
        LoginResult<EmployeeLoginResponseDto> result = employeeAuthUsecase.login(request);
        ResponseCookie cookie = AccessTokenCookie.issue(result.accessToken(), result.expiresIn());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.ok("Login Successful", result.user()));
    }

    @PostMapping("/r/login")
    public ResponseEntity<ApiResponse<RetailerLoginResponseDto>> retailerLogin(
            @Valid @RequestBody RetailerLoginRequestDto request) {
        LoginResult<RetailerLoginResponseDto> result = retailerAuthUseCase.login(request);
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
