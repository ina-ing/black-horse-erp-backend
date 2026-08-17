package com.inaing.blackhorse_erp.module.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFilter;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderQueryParams;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.GetAllOrdersUsecase;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

/**
 * The warehouse sees every retailer's orders, but only the statuses it works:
 * approved, processing, partially fulfilled and completed.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderWarehouseScopeTest {

    private static final List<OrderStatus> WORKED_STATUSES = List.of(
            OrderStatus.APPROVED,
            OrderStatus.PROCESSING,
            OrderStatus.PARTIAL,
            OrderStatus.COMPLETED);

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private IOrderService orderService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private GetAllOrdersUsecase usecase;

    @Test
    void warehouseSeesEveryRetailerButOnlyTheStatusesItWorks() {
        OrderFilter filter = executeAsWarehouse(new OrderQueryParams());

        assertThat(filter.handledById()).isNull();
        assertThat(filter.retailerId()).isNull();
        assertThat(filter.statuses()).containsExactlyElementsOf(WORKED_STATUSES);
        assertThat(filter.statuses())
                .doesNotContain(OrderStatus.PENDING, OrderStatus.CANCELLED);
    }

    @Test
    void warehouseAskingForAWorkedStatusGetsThatStatus() {
        OrderQueryParams params = new OrderQueryParams();
        params.setStatuses(List.of(OrderStatus.PROCESSING));

        OrderFilter filter = executeAsWarehouse(params);

        assertThat(filter.statuses()).containsExactly(OrderStatus.PROCESSING);
    }

    @Test
    void warehouseAskingForPendingGetsNothing() {
        OrderQueryParams params = new OrderQueryParams();
        params.setStatuses(List.of(OrderStatus.PENDING));

        OrderFilter filter = executeAsWarehouse(params);

        // Empty means "match nothing" in the specification, never "match everything".
        assertThat(filter.statuses()).isEmpty();
    }

    @Test
    void warehouseAskingForAMixKeepsOnlyTheWorkedStatuses() {
        OrderQueryParams params = new OrderQueryParams();
        params.setStatuses(List.of(OrderStatus.PENDING, OrderStatus.COMPLETED));

        OrderFilter filter = executeAsWarehouse(params);

        assertThat(filter.statuses()).containsExactly(OrderStatus.COMPLETED);
    }

    @Test
    void warehouseCountsCoverOnlyTheStatusesItWorks() {
        executeAsWarehouse(new OrderQueryParams());

        verify(orderService).getStatusCounts(null, null, WORKED_STATUSES);
    }

    @Test
    void adminKeepsEveryStatus() {
        when(currentUserProvider.currentPrincipal())
                .thenReturn(Optional.of(new AuthPrincipal("admin-1", "Admin", Role.ADMIN.name())));
        stubService();

        usecase.execute(new OrderQueryParams());

        assertThat(capturedFilter().statuses())
                .containsExactlyInAnyOrder(OrderStatus.values());
    }

    private OrderFilter executeAsWarehouse(OrderQueryParams params) {
        when(currentUserProvider.currentPrincipal()).thenReturn(
                Optional.of(new AuthPrincipal("warehouse-1", "Depot", Role.WAREHOUSE.name())));
        stubService();

        usecase.execute(params);

        return capturedFilter();
    }

    private void stubService() {
        Page<Order> empty = new PageImpl<>(List.of());
        when(orderService.getOrders(any(), any())).thenReturn(empty);
        when(orderService.getStatusCounts(any(), any(), any())).thenReturn(List.of());
        when(orderService.getMonthlyVolume(any(), any(), any(), any())).thenReturn(List.of());
    }

    private OrderFilter capturedFilter() {
        ArgumentCaptor<OrderFilter> filter = ArgumentCaptor.forClass(OrderFilter.class);
        verify(orderService).getOrders(filter.capture(), ArgumentCaptor.forClass(Pageable.class).capture());
        return filter.getValue();
    }
}
