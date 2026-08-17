package com.inaing.blackhorse_erp.module.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Sort;

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
 * The caller's role decides the scope of the list: a retailer is pinned to its
 * own orders while every other part of the query behaves as it does for admin.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetAllOrdersUsecaseScopeTest {

    private static final String RETAILER_ID = "retailer-1";
    private static final String SALESMAN_ID = "salesman-1";

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private IOrderService orderService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private GetAllOrdersUsecase usecase;

    @Test
    void retailerIsScopedToItsOwnOrders() {
        OrderFilter filter = executeAs(Role.RETAILER, RETAILER_ID, new OrderQueryParams());

        assertThat(filter.retailerId()).isEqualTo(RETAILER_ID);
        assertThat(filter.handledById()).isNull();
    }

    @Test
    void adminIsNotScoped() {
        OrderFilter filter = executeAs(Role.ADMIN, "admin-1", new OrderQueryParams());

        assertThat(filter.retailerId()).isNull();
        assertThat(filter.handledById()).isNull();
    }

    @Test
    void salesmanIsScopedToTheOrdersItHandles() {
        OrderFilter filter = executeAs(Role.SALES, SALESMAN_ID, new OrderQueryParams());

        assertThat(filter.handledById()).isEqualTo(SALESMAN_ID);
        assertThat(filter.retailerId()).isNull();
    }

    @Test
    void retailerKeepsTheSameFilteringAndPagingAsAdmin() {
        OrderQueryParams params = new OrderQueryParams();
        params.setPage(2);
        params.setSize(25);
        params.setSortBy("orderQuantity");
        params.setSortDir("asc");
        params.setSearch("ORD-1");
        params.setStatuses(List.of(OrderStatus.CANCELLED));

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        OrderFilter filter = executeAs(Role.RETAILER, RETAILER_ID, params, pageable);

        assertThat(filter.statuses()).containsExactly(OrderStatus.CANCELLED);
        assertThat(filter.search()).isEqualTo("ORD-1");
        assertThat(pageable.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageable.getValue().getPageSize()).isEqualTo(25);
        assertThat(pageable.getValue().getSort().getOrderFor("totalQuantity"))
                .isNotNull()
                .satisfies(order -> assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC));
    }

    @Test
    void aRetailerCannotWidenItsScopeThroughTheRetailerParam() {
        OrderQueryParams params = new OrderQueryParams();
        params.setRetailer("RET-SOMEONE-ELSE");

        OrderFilter filter = executeAs(Role.RETAILER, RETAILER_ID, params);

        // Both predicates are applied, so the extra param can only ever narrow.
        assertThat(filter.retailerId()).isEqualTo(RETAILER_ID);
        assertThat(filter.retailer()).isEqualTo("RET-SOMEONE-ELSE");
    }

    private OrderFilter executeAs(Role role, String id, OrderQueryParams params) {
        return executeAs(role, id, params, ArgumentCaptor.forClass(Pageable.class));
    }

    private OrderFilter executeAs(Role role, String id, OrderQueryParams params,
            ArgumentCaptor<Pageable> pageable) {

        when(currentUserProvider.currentPrincipal())
                .thenReturn(Optional.of(new AuthPrincipal(id, "Tester", role.name())));

        Page<Order> empty = new PageImpl<>(List.of());
        when(orderService.getOrders(any(), any())).thenReturn(empty);
        when(orderService.getStatusCounts(any(), any(), any())).thenReturn(List.of());
        when(orderService.getMonthlyVolume(any(), any(), any(), any())).thenReturn(List.of());

        usecase.execute(params);

        ArgumentCaptor<OrderFilter> filter = ArgumentCaptor.forClass(OrderFilter.class);
        org.mockito.Mockito.verify(orderService).getOrders(filter.capture(), pageable.capture());

        return filter.getValue();
    }
}
