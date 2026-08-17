package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

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
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderFilter;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderQueryParams;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderListResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.analytics.ProductionOrderBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllProductionOrdersUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "productionOrderDate", "orderDate",
            "orderDate", "orderDate",
            "quantity", "totalQuantity",
            "totalArticles", "totalArticles");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "orderDate");

    private final ProductionOrderMapper productionOrderMapper;
    private final IProductionOrderService productionOrderService;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<ProductionOrderBasicAnalyticsDto, ProductionOrderListResponseDto> execute(
            ProductionOrderQueryParams params) {

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        ProductionOrderFilter filter = new ProductionOrderFilter(
                dateRange, params.getStatuses(), params.getSearch());

        Page<ProductionOrderListResponseDto> page = productionOrderService
                .getProductionOrders(filter, pageable)
                .map(productionOrderMapper::toListResponse);

        return PagedListWithAnalytics.of(analytics(), PageResponse.of(page));
    }

    // Counts ignore the list filters, so the stat cards stay stable while filtering.
    private ProductionOrderBasicAnalyticsDto analytics() {

        Map<ProductionOrderStatus, Long> byStatus = new EnumMap<>(ProductionOrderStatus.class);
        for (ProductionOrderStatus status : ProductionOrderStatus.values()) {
            byStatus.put(status, 0L);
        }

        long total = 0;
        long totalQuantity = 0;
        for (var point : productionOrderService.getAnalytics()) {
            byStatus.put(point.getStatus(), point.getCount());
            total += point.getCount();
            totalQuantity += point.getQuantity();
        }

        return new ProductionOrderBasicAnalyticsDto(total, totalQuantity, byStatus);
    }
}
