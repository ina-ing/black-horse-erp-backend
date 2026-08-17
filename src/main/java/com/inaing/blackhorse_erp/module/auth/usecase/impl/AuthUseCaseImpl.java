package com.inaing.blackhorse_erp.module.auth.usecase.impl;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.auth.dto.LoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.LoginUserDto;
import com.inaing.blackhorse_erp.module.auth.mapper.AuthMapper;
import com.inaing.blackhorse_erp.module.auth.usecase.IAuthUseCase;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.security.jwt.JwtService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements IAuthUseCase {

    private static final String REALM_EMPLOYEE = "EMPLOYEE";
    private static final String REALM_RETAILER = "RETAILER";
    private static final String ROLE_RETAILER = "RETAILER";
    private static final String ROLE_UNKNOWN = "UNKNOWN";

    private final IEmployeeService employeeService;
    private final IRetailerService retailerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final IActivityLogService activityLogService;

    @Override
    @Transactional(readOnly = true)
    public LoginResult<LoginUserDto> login(LoginRequestDto request) {

        Employee employee = employeeService.findByPhone(request.phone());
        if (employee != null) {
            return loginEmployee(employee, request.password());
        }

        Retailer retailer = retailerService.findByPhone(request.phone());
        if (retailer != null) {
            return loginRetailer(retailer, request.password());
        }

        recordFailedLogin(request.phone());
        throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    private LoginResult<LoginUserDto> loginEmployee(Employee employee, String password) {
        if (!matches(password, employee.getPasswordHash()) || !employee.isActive()) {
            recordFailedLogin(employee.getPhone());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String role = employee.getRole().name();

        String token = jwtService.issueAccessToken(String.valueOf(employee.getId()), Map.of(
                JwtService.CLAIM_UID, employee.getId(),
                JwtService.CLAIM_ROLE, role,
                JwtService.CLAIM_REALM, REALM_EMPLOYEE,
                JwtService.CLAIM_NAME, employee.getFullname()));

        recordSuccessfulLogin(new AuthPrincipal(employee.getId(), employee.getFullname(), role));

        return new LoginResult<>(token, jwtService.accessTokenTtl().toSeconds(),
                authMapper.toEmployeeLoginResponseDto(employee));
    }

    private LoginResult<LoginUserDto> loginRetailer(Retailer retailer, String password) {
        if (!matches(password, retailer.getPasswordHash())) {
            recordFailedLogin(retailer.getPhone());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.issueAccessToken(String.valueOf(retailer.getId()), Map.of(
                JwtService.CLAIM_UID, retailer.getId(),
                JwtService.CLAIM_ROLE, ROLE_RETAILER,
                JwtService.CLAIM_REALM, REALM_RETAILER,
                JwtService.CLAIM_NAME, retailer.getStoreName()));

        recordSuccessfulLogin(
                new AuthPrincipal(retailer.getId(), retailer.getStoreName(), ROLE_RETAILER));

        return new LoginResult<>(token, jwtService.accessTokenTtl().toSeconds(),
                authMapper.toRetailerLoginResponseDto(retailer));
    }

    private void recordSuccessfulLogin(AuthPrincipal actor) {
        activityLogService.record(actor, ActivityAction.LOGIN_SUCCEEDED,
                actor.name() + " signed in.",
                ActivityEntityType.AUTH, actor.id(), null, ActionTrigger.MANUAL);
    }

    private void recordFailedLogin(String phone) {
        activityLogService.record(new AuthPrincipal(null, phone, ROLE_UNKNOWN),
                ActivityAction.LOGIN_FAILED,
                "Failed sign in attempt for " + phone + ".",
                ActivityEntityType.AUTH, null, null, ActionTrigger.MANUAL);
    }

    private boolean matches(String password, String passwordHash) {
        return passwordHash != null && passwordEncoder.matches(password, passwordHash);
    }
}
