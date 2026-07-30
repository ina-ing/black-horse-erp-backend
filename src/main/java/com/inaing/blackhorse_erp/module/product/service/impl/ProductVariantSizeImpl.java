package com.inaing.blackhorse_erp.module.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.repository.ProductVariantSizeRepository;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantSizeImpl implements IProductVariantSizeService {

    private final ProductVariantSizeRepository variantSizeRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductVariantSize getById(String id) {
        return variantSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Variant size not found " + id));
    }

}
