package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
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
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionFilter;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionQueryParams;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionListResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.analytics.ProductionBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.production.mapper.ProductionMapper;
import com.inaing.blackhorse_erp.module.production.service.IProductionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllProductionUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "productionDate", "productionDate",
            "quantity", "totalQuantity",
            "totalArticles", "totalArticles");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "productionDate");

    private final ProductionMapper productionMapper;
    private final IProductionService productionService;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<ProductionBasicAnalyticsDto, ProductionListResponseDto> execute(
            ProductionQueryParams params) {

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        ProductionFilter filter = new ProductionFilter(
                dateRange, params.getFactory(), params.getSearch());

        Page<ProductionListResponseDto> page = productionService.getProductions(filter, pageable)
                .map(productionMapper::toListResponse);

        return PagedListWithAnalytics.of(analytics(), PageResponse.of(page));
    }

    private ProductionBasicAnalyticsDto analytics() {
        Instant monthStart = YearMonth.now()
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        return ProductionBasicAnalyticsDto.of(
                productionService.getTotals(), productionService.countSince(monthStart));
    }
}
