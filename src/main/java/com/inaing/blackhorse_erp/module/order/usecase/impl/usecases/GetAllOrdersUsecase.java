package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.common.dto.pagination.DatePeriod;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRangeResolver;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.projections.OrderMonthlyVolumeProjection;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFilter;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderQueryParams;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderListResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.analytics.OrderBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.order.dto.response.analytics.OrderMonthlyVolumeDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllOrdersUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "orderDate", "orderDate",
            "orderQuantity", "totalQuantity",
            "totalArticles", "totalArticles");

    private static final int VOLUME_MONTHS = 6;

    private static final List<OrderStatus> ALL_STATUSES = Arrays.asList(OrderStatus.values());

    // The warehouse works orders once they are approved; it has no business with
    // the ones still awaiting approval or already cancelled.
    private static final List<OrderStatus> WAREHOUSE_STATUSES = List.of(
            OrderStatus.APPROVED,
            OrderStatus.PROCESSING,
            OrderStatus.PARTIAL,
            OrderStatus.COMPLETED);

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "orderDate");

    private final OrderMapper orderMapper;
    private final IOrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<OrderBasicAnalyticsDto, OrderListResponseDto> execute(
            OrderQueryParams params) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(principal.role());
        String handledById = role == Role.SALES ? principal.id() : null;
        String retailerId = role == Role.RETAILER ? principal.id() : null;
        List<OrderStatus> visibleStatuses = statusesVisibleTo(role);

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        OrderFilter filter = new OrderFilter(
                dateRange, statuses(visibleStatuses, params.getStatuses()), params.getSearch(),
                handledById, retailerId, params.getRetailer());

        Page<OrderListResponseDto> page = orderService.getOrders(filter, pageable)
                .map(orderMapper::toListResponse);

        return PagedListWithAnalytics.of(
                analytics(handledById, retailerId, visibleStatuses), PageResponse.of(page));
    }

    private List<OrderStatus> statusesVisibleTo(Role role) {
        return role == Role.WAREHOUSE ? WAREHOUSE_STATUSES : ALL_STATUSES;
    }

    // A requested status narrows what the role may see; asking for one outside that
    // set matches nothing rather than quietly widening the scope.
    private List<OrderStatus> statuses(List<OrderStatus> visible, List<OrderStatus> requested) {
        if (requested == null || requested.isEmpty()) {
            return visible;
        }
        return requested.stream().filter(visible::contains).toList();
    }

    // Counts every status so the caller can read whichever buckets it shows, and a
    // volume point per month for the last window with the quiet months filled in.
    private OrderBasicAnalyticsDto analytics(String handledById, String retailerId,
            List<OrderStatus> visibleStatuses) {

        Map<OrderStatus, Long> byStatus = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            byStatus.put(status, 0L);
        }
        orderService.getStatusCounts(handledById, retailerId, visibleStatuses)
                .forEach(count -> byStatus.put(count.getStatus(), count.getCount()));

        long total = byStatus.values().stream().mapToLong(Long::longValue).sum();

        return new OrderBasicAnalyticsDto(
                total, byStatus, monthlyVolume(handledById, retailerId, visibleStatuses));
    }

    private List<OrderMonthlyVolumeDto> monthlyVolume(String handledById, String retailerId,
            List<OrderStatus> visibleStatuses) {
        YearMonth firstMonth = YearMonth.now().minusMonths(VOLUME_MONTHS - 1L);
        Instant from = firstMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        Map<YearMonth, Long> byMonth = orderService
                .getMonthlyVolume(from, handledById, retailerId, visibleStatuses)
                .stream()
                .collect(Collectors.toMap(
                        point -> YearMonth.of(point.getYear(), point.getMonth()),
                        OrderMonthlyVolumeProjection::getQuantity));

        List<OrderMonthlyVolumeDto> volume = new ArrayList<>(VOLUME_MONTHS);
        for (int i = 0; i < VOLUME_MONTHS; i++) {
            YearMonth month = firstMonth.plusMonths(i);
            volume.add(new OrderMonthlyVolumeDto(
                    month.getYear(), month.getMonthValue(), byMonth.getOrDefault(month, 0L)));
        }
        return volume;
    }
}
