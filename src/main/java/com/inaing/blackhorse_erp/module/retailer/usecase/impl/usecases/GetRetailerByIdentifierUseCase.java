package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetRetailerByIdentifierUseCase {

    private final IRetailerService retailerService;
    private final RetailerMapper retailerMapper;

    @Transactional(readOnly = true)
    public RetailerResponseDto execute(String identifier) {
        Retailer retailer = retailerService.getByIdentifier(identifier);

        if (retailer == null) {
            throw new AppException(ErrorCode.RETAILER_NOT_FOUND);
        }

        return retailerMapper.toResponse(retailer);
    }
}
