package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductVariantStatusUsecase {

    private final ProductMapper productMapper;
    private final IProductVariantService productVariantService;
    private final IActivityLogService activityLogService;

    @Transactional
    public ProductVariantResponseDto execute(String id, ProductStatusUpdateRequestDto request) {

        ProductVariant variant = productVariantService.getById(id);

        if (request.status() != ProductStatus.ACTIVE && request.status() != ProductStatus.INACTIVE) {
            throw new BusinessRuleException(
                    "INVALID_STATUS_VALUE",
                    "Product variant status can only be updated to active or inactive.");
        }

        variant.setStatus(request.status());
        ProductVariant saved = productVariantService.update(variant);

        activityLogService.record(
                saved.getStatus() == ProductStatus.ACTIVE
                        ? ActivityAction.PRODUCT_ACTIVATED
                        : ActivityAction.PRODUCT_VARIANT_DEACTIVATED,
                (saved.getStatus() == ProductStatus.ACTIVE ? "Activated colorway "
                        : "Deactivated colorway ") + saved.getColor() + " of "
                        + saved.getProduct().getArticleCode() + ".",
                ActivityEntityType.PRODUCT, saved.getId(),
                saved.getProduct().getArticleCode(), ActionTrigger.MANUAL);

        return productMapper.toVariantResponse(saved);
    }
}
