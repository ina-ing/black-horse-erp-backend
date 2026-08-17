package com.inaing.blackhorse_erp.module.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFilter;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.order.service.impl.OrderServiceImpl;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.persistence.EntityManager;

/**
 * A salesman reads back the orders they handled, in every status, with the same
 * paging, sorting and filtering the unscoped (admin) query gives.
 */
@DataJpaTest
@Import(OrderServiceImpl.class)
class OrderSalesScopeTest {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private EntityManager entityManager;

    private Employee salesman;
    private Employee otherSalesman;
    private Retailer reassignedRetailer;

    @BeforeEach
    void seed() {
        salesman = persist(employee("EMP-001", "Ram Bahadur", "9800000001"));
        otherSalesman = persist(employee("EMP-002", "Hari Shrestha", "9800000002"));

        Retailer own = persist(retailer("RET-001", "ABC Mart", "9811111111", salesman));
        Retailer foreign = persist(retailer("RET-002", "Hill Top Traders", "9822222222", otherSalesman));

        // A retailer that has since been handed over to the other salesman, but whose
        // earlier orders were handled by ours.
        reassignedRetailer = persist(
                retailer("RET-003", "Metro Supplies", "9833333333", otherSalesman));

        int day = 1;
        for (OrderStatus status : OrderStatus.values()) {
            persist(order(own, salesman, "ORD-1" + day, status, day));
            day++;
        }

        persist(order(reassignedRetailer, salesman, "ORD-500", OrderStatus.COMPLETED, 7));

        persist(order(foreign, otherSalesman, "ORD-901", OrderStatus.PENDING, 1));
        persist(order(foreign, otherSalesman, "ORD-902", OrderStatus.CANCELLED, 2));

        entityManager.flush();
        entityManager.clear();
    }

    // Six own-retailer orders plus the one for the reassigned retailer.
    private int handledByOurSalesman() {
        return OrderStatus.values().length + 1;
    }

    @Test
    @DisplayName("scoped query returns only the orders the salesman handled")
    void returnsOnlyHandledOrders() {
        Page<Order> page = orderService.getOrders(salesFilter(null), unpaged());

        assertThat(page.getTotalElements()).isEqualTo(handledByOurSalesman());
        assertThat(page.getContent())
                .allSatisfy(order -> assertThat(order.getHandledBy().getId())
                        .isEqualTo(salesman.getId()));
    }

    @Test
    @DisplayName("cancelled orders stay visible to the salesman")
    void includesCancelledOrders() {
        Page<Order> page = orderService.getOrders(salesFilter(null), unpaged());

        assertThat(page.getContent())
                .extracting(Order::getStatus)
                .contains(OrderStatus.CANCELLED)
                .containsAll(List.of(OrderStatus.values()));
    }

    @Test
    @DisplayName("status filter narrows within the salesman's own orders")
    void statusFilterStaysScoped() {
        Page<Order> cancelled = orderService.getOrders(
                salesFilter(List.of(OrderStatus.CANCELLED)), unpaged());

        assertThat(cancelled.getTotalElements()).isEqualTo(1);
        assertThat(cancelled.getContent().get(0).getHandledBy().getId())
                .isEqualTo(salesman.getId());
    }

    @Test
    @DisplayName("search cannot reach another salesman's orders")
    void searchStaysScoped() {
        Page<Order> foreign = orderService.getOrders(
                new OrderFilter(null, null, "Hill Top", salesman.getId(), null, null), unpaged());

        assertThat(foreign.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("paging counts only the salesman's own orders")
    void pagingIsScopedAndConsistent() {
        Sort byDate = Sort.by(Sort.Direction.DESC, "orderDate");

        Page<Order> first = orderService.getOrders(salesFilter(null), PageRequest.of(0, 3, byDate));
        Page<Order> second = orderService.getOrders(salesFilter(null), PageRequest.of(1, 3, byDate));

        assertThat(first.getTotalElements()).isEqualTo(handledByOurSalesman());
        assertThat(first.getContent()).hasSize(3);
        assertThat(first.getContent()).doesNotContainAnyElementsOf(second.getContent());
        assertThat(second.getContent())
                .allSatisfy(order -> assertThat(order.getHandledBy().getId())
                        .isEqualTo(salesman.getId()));
    }

    /**
     * Documents today's behaviour: the scope follows who handled the order, not who
     * the retailer is assigned to now. An order handled by this salesman stays with
     * them after the retailer moves to somebody else.
     */
    @Test
    @DisplayName("handled orders survive a retailer reassignment")
    void scopeFollowsHandlerNotCurrentAssignment() {
        Page<Order> ours = orderService.getOrders(salesFilter(null), unpaged());

        assertThat(ours.getContent())
                .extracting(Order::getCode)
                .contains("ORD-500");

        assertThat(reassignedRetailer.getAssignedSalesman().getId())
                .isEqualTo(otherSalesman.getId());
    }

    private OrderFilter salesFilter(List<OrderStatus> statuses) {
        return new OrderFilter(null, statuses, null, salesman.getId(), null, null);
    }

    private PageRequest unpaged() {
        return PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    private Employee employee(String code, String fullname, String phone) {
        return Employee.builder()
                .code(code)
                .fullname(fullname)
                .phone(phone)
                .role(Role.SALES)
                .joinedOn(LocalDate.now())
                .build();
    }

    private Retailer retailer(String code, String storeName, String phone, Employee assigned) {
        return Retailer.builder()
                .code(code)
                .storeName(storeName)
                .contactPerson("Owner " + code)
                .phone(phone)
                .passwordHash("hash")
                .assignedSalesman(assigned)
                .joinedOn(LocalDate.now())
                .build();
    }

    private Order order(Retailer owner, Employee handler, String code, OrderStatus status, int day) {
        return Order.builder()
                .code(code)
                .retailer(owner)
                .handledBy(handler)
                .status(status)
                .createdByRole(Role.SALES)
                .orderDate(Instant.parse("2026-01-0" + day + "T10:00:00Z"))
                .totalQuantity(day * 10)
                .totalArticles(day)
                .build();
    }

    private <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }
}
