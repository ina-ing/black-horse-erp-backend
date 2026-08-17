package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerFilter;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerQueryParams;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetRetailersListUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "storeName", "storeName",
            "joinedOn", "joinedOn");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "storeName");

    private final IRetailerService retailerService;
    private final RetailerMapper retailerMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto> execute(
            RetailerQueryParams params) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // A salesman only ever sees the retailers assigned to them.
        String assignedSalesmanId = Role.fromName(principal.role()) == Role.SALES
                ? principal.id()
                : null;

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        RetailerFilter filter = new RetailerFilter(
                params.getProvince(), params.getBusinessType(), params.getSearch(),
                assignedSalesmanId);

        Page<RetailerResponseDto> page = retailerService.getRetailers(filter, pageable)
                .map(retailerMapper::toResponse);

        Map<String, Long> counts = retailerService.getRetailerCounts(assignedSalesmanId);
        RetailerListAnalyticsDto analytics = RetailerListAnalyticsDto.builder()
                .totalRetailers(counts.get("TOTAL"))
                .newRetailers(counts.get("NEW"))
                .provincesCovered(counts.get("PROVINCES"))
                .build();

        return PagedListWithAnalytics.of(analytics, PageResponse.of(page));
    }
}
