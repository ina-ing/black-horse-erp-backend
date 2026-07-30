package com.inaing.blackhorse_erp.module.auth.usecase.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.employee.EmployeeLoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.employee.EmployeeLoginResponseDto;
import com.inaing.blackhorse_erp.module.auth.mapper.AuthMapper;
import com.inaing.blackhorse_erp.module.auth.usecase.IAuthUseCase;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Qualifier("employeeAuthUseCase")
public class EmployeeAuthUseCaseImpl implements IAuthUseCase<EmployeeLoginRequestDto, EmployeeLoginResponseDto> {

    private static final String REALM_EMPLOYEE = "EMPLOYEE";

    private final IEmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResult<EmployeeLoginResponseDto> login(EmployeeLoginRequestDto request) {
        Employee employee = employeeService.findByPhone(request.phone());

        if (employee == null
                || !passwordEncoder.matches(request.password(), employee.getPasswordHash())
                || !employee.isActive()) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String role = employee.getRole().name();

        String token = jwtService.issueAccessToken(String.valueOf(employee.getId()), Map.of(
                JwtService.CLAIM_UID, employee.getId(),
                JwtService.CLAIM_ROLE, role,
                JwtService.CLAIM_REALM, REALM_EMPLOYEE,
                JwtService.CLAIM_NAME, employee.getFullname()));

        EmployeeLoginResponseDto user = authMapper.toEmployeeLoginResponseDto(employee);

        return new LoginResult<EmployeeLoginResponseDto>(token, jwtService.accessTokenTtl().toSeconds(), user);
    }

}
