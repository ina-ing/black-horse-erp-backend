package com.inaing.blackhorse_erp.module.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.repository.ProductVariantRepository;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductVariant getById(String id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Product variant not found " + id));
    }

    @Override
    @Transactional
    public ProductVariant update(ProductVariant variant) {
        return productVariantRepository.save(variant);
    }

}
