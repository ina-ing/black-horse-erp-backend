package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAssignedRetailersUsecase {

    private final RetailerMapper retailerMapper;
    private final IRetailerService retailerService;
    private final CurrentUserProvider currentUserProvider;

    public List<RetailerResponseDto> execute() {
        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        return retailerService.getAssignedRetailers(principal.id())
                .stream()
                .map(retailerMapper::toResponse)
                .toList();
    }
}
