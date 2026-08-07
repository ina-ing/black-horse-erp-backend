package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnItem;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnItemRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateReturnUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IProductVariantSizeService variantSizeService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public ReturnResponseDto execute(String id, ReturnUpdateRequestDto request) {

        Return ret = returnService.getByIdentifier(id);
        if (ret == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Return not found " + id);
        }
        if (ret.getStatus() != ReturnStatus.PENDING) {
            throw new BusinessRuleException(
                    "RETURN_NOT_EDITABLE",
                    "Only pending returns can be updated.");
        }

        AuthPrincipal editor = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        Role role = Role.fromName(editor.role());
        switch (role) {
            case RETAILER -> {
                if (!ret.getRetailer().getId().equals(editor.id())) {
                    throw new AppException(ErrorCode.RETURN_UPDATE_DENIED);
                }
            }
            case SALES -> {
                if (ret.getHandledBy() == null
                        || !ret.getHandledBy().getId().equals(editor.id())) {
                    throw new AppException(ErrorCode.RETURN_UPDATE_DENIED);
                }
            }
            default -> throw new AppException(ErrorCode.RETURN_UPDATE_DENIED);
        }
        
        updateItems(ret, dedupeItems(request.items()));

        if (request.note() != null) {
            ret.setNote(request.note());
        }
        if (request.reason() != null) {
            ret.setReason(ReturnReason.fromName(request.reason()));
        }
        ret.recalculateTotals();
        return returnMapper.toResponse(returnService.update(ret));
    }

    private List<ReturnItemRequestDto> dedupeItems(List<ReturnItemRequestDto> items) {
        Map<String, Integer> merged = new LinkedHashMap<>();

        for (ReturnItemRequestDto item : items) {
            merged.merge(
                    item.variantSizeId(),
                    item.quantity(),
                    (existing, incoming) -> existing + incoming);
        }

        return merged.entrySet()
                .stream()
                .map(entry -> new ReturnItemRequestDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void updateItems(Return ret, List<ReturnItemRequestDto> requests) {

        Map<String, ReturnItem> existingItems = ret.getItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getVariantSize().getId(),
                        Function.identity()));

        for (ReturnItemRequestDto request : requests) {

            ReturnItem item = existingItems.remove(request.variantSizeId());

            if (item == null) {
                ProductVariantSize variant = variantSizeService.getById(request.variantSizeId());

                item = ReturnItem.builder()
                        .variantSize(variant)
                        .build();

                ret.addItem(item);
            }

            item.setQuantity(request.quantity());
        }

        existingItems.values().forEach(ret::removeItem);
    }
}
