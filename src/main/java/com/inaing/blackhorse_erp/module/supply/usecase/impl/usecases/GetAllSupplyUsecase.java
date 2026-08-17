package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.common.dto.pagination.DatePeriod;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRangeResolver;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyFilter;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyQueryParams;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyListResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.analytics.SupplyBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllSupplyUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "productionSupplyDate", "supplyDate",
            "supplyDate", "supplyDate",
            "quantity", "totalQuantity",
            "totalArticles", "totalArticles");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "supplyDate");

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<SupplyBasicAnalyticsDto, SupplyListResponseDto> execute(
            SupplyQueryParams params) {

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        SupplyFilter filter = new SupplyFilter(
                dateRange, params.getStatuses(), params.getSearch());

        Page<SupplyListResponseDto> page = supplyService.getSupplies(filter, pageable)
                .map(supplyMapper::toListResponse);

        return PagedListWithAnalytics.of(analytics(), PageResponse.of(page));
    }

    private SupplyBasicAnalyticsDto analytics() {

        Map<SupplyStatus, Long> byStatus = new EnumMap<>(SupplyStatus.class);
        for (SupplyStatus status : SupplyStatus.values()) {
            byStatus.put(status, 0L);
        }
        supplyService.getStatusCounts()
                .forEach(count -> byStatus.put(count.getStatus(), count.getCount()));

        long total = byStatus.values().stream().mapToLong(Long::longValue).sum();

        return new SupplyBasicAnalyticsDto(total, byStatus);
    }
}
