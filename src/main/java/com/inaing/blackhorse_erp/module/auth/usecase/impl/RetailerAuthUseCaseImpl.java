package com.inaing.blackhorse_erp.module.auth.usecase.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.retailer.RetailerLoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.retailer.RetailerLoginResponseDto;
import com.inaing.blackhorse_erp.module.auth.mapper.AuthMapper;
import com.inaing.blackhorse_erp.module.auth.usecase.IAuthUseCase;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Qualifier("retailerAuthUseCase")
public class RetailerAuthUseCaseImpl implements IAuthUseCase<RetailerLoginRequestDto, RetailerLoginResponseDto> {

    private static final String REALM_RETAILER = "RETAILER";
    private static final String ROLE_RETAILER = "RETAILER";

    private final PasswordEncoder passwordEncoder;
    private final IRetailerService retailerService;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResult<RetailerLoginResponseDto> login(RetailerLoginRequestDto request) {
        Retailer retailer = retailerService.findByPhone(request.phone());

        if (retailer == null
                || !passwordEncoder.matches(request.password(), retailer.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.issueAccessToken(String.valueOf(retailer.getId()), Map.of(
                JwtService.CLAIM_UID, retailer.getId(),
                JwtService.CLAIM_ROLE, ROLE_RETAILER,
                JwtService.CLAIM_REALM, REALM_RETAILER,
                JwtService.CLAIM_NAME, retailer.getStoreName()));

        RetailerLoginResponseDto user = authMapper.toRetailerLoginResponseDto(retailer);

        return new LoginResult<RetailerLoginResponseDto>(token, jwtService.accessTokenTtl().toSeconds(), user);
    }

}
