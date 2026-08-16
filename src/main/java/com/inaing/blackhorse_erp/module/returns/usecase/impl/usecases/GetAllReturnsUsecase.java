package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnListResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllReturnsUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<ReturnListResponseDto> execute(ReturnStatus status) {

        List<Return> returns = status != null
                ? returnService.getAllByStatus(status)
                : returnService.getAll();

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if (Role.fromName(principal.role()) == Role.SALES) {
            returns = returns.stream()
                    .filter(ret -> ret.getHandledBy() != null
                            && ret.getHandledBy().getId().equals(principal.id()))
                    .toList();
        }
        if (Role.fromName(principal.role()) == Role.RETAILER) {
            returns = returns.stream()
                    .filter(ret -> ret.getRetailer() != null
                            && ret.getRetailer().getId().equals(principal.id()))
                    .toList();
        }
        if (Role.fromName(principal.role()) == Role.WAREHOUSE) {
            returns = returns.stream()
                    .filter(ret -> ret.getReason() == ReturnReason.STOCK)
                    .toList();
        }
        if (Role.fromName(principal.role()) == Role.FACTORY) {
            returns = returns.stream()
                    .filter(ret -> ret.getReason() == ReturnReason.DAMAGE
                            || ret.getReason() == ReturnReason.REPAIR)
                    .toList();
        }

        return returns.stream()
                .map(returnMapper::toListResponse)
                .toList();
    }
}
