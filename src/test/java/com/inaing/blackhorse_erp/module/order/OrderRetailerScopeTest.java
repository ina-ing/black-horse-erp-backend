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
 * A retailer must only ever read back its own orders, and the paging, sorting
 * and filtering it gets must behave the same as the unscoped (admin) query.
 */
@DataJpaTest
@Import(OrderServiceImpl.class)
class OrderRetailerScopeTest {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private EntityManager entityManager;

    private Retailer retailer;
    private Retailer otherRetailer;

    @BeforeEach
    void seed() {
        Employee salesman = persist(Employee.builder()
                .code("EMP-001")
                .fullname("Ram Bahadur")
                .phone("9800000001")
                .role(Role.SALES)
                .joinedOn(LocalDate.now())
                .build());

        retailer = persist(retailer("RET-001", "ABC Mart", "9811111111", salesman));
        otherRetailer = persist(retailer("RET-002", "Hill Top Traders", "9822222222", salesman));

        // Six for our retailer, one per status, so every bucket is represented.
        int day = 1;
        for (OrderStatus status : OrderStatus.values()) {
            persist(order(retailer, salesman, "ORD-1" + day, status, day));
            day++;
        }

        // Two that belong to somebody else and must never show up.
        persist(order(otherRetailer, salesman, "ORD-901", OrderStatus.PENDING, 1));
        persist(order(otherRetailer, salesman, "ORD-902", OrderStatus.CANCELLED, 2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("scoped query returns only the retailer's own orders")
    void returnsOnlyOwnOrders() {
        Page<Order> page = orderService.getOrders(retailerFilter(null), unpaged());

        assertThat(page.getTotalElements()).isEqualTo(OrderStatus.values().length);
        assertThat(page.getContent())
                .allSatisfy(order -> assertThat(order.getRetailer().getId())
                        .isEqualTo(retailer.getId()));
    }

    @Test
    @DisplayName("cancelled orders stay visible to the retailer")
    void includesCancelledOrders() {
        Page<Order> all = orderService.getOrders(retailerFilter(null), unpaged());

        assertThat(all.getContent())
                .extracting(Order::getStatus)
                .contains(OrderStatus.CANCELLED)
                .containsExactlyInAnyOrder(OrderStatus.values());
    }

    @Test
    @DisplayName("status filter narrows within the retailer's own orders")
    void statusFilterStaysScoped() {
        Page<Order> cancelled = orderService.getOrders(
                retailerFilter(List.of(OrderStatus.CANCELLED)), unpaged());

        assertThat(cancelled.getTotalElements()).isEqualTo(1);
        assertThat(cancelled.getContent().get(0).getRetailer().getId())
                .isEqualTo(retailer.getId());
    }

    @Test
    @DisplayName("search matches the retailer's own orders only")
    void searchStaysScoped() {
        // ORD-9xx belongs to the other retailer; the term must find nothing.
        Page<Order> foreign = orderService.getOrders(
                new OrderFilter(null, null, "ORD-9", null, retailer.getId(), null), unpaged());

        assertThat(foreign.getTotalElements()).isZero();

        Page<Order> own = orderService.getOrders(
                new OrderFilter(null, null, "ABC MART", null, retailer.getId(), null), unpaged());

        assertThat(own.getTotalElements()).isEqualTo(OrderStatus.values().length);
    }

    @Test
    @DisplayName("paging counts only the retailer's own orders")
    void pagingIsScopedAndConsistent() {
        Sort byDate = Sort.by(Sort.Direction.DESC, "orderDate");

        Page<Order> first = orderService.getOrders(
                retailerFilter(null), PageRequest.of(0, 2, byDate));
        Page<Order> second = orderService.getOrders(
                retailerFilter(null), PageRequest.of(1, 2, byDate));

        assertThat(first.getTotalElements()).isEqualTo(OrderStatus.values().length);
        assertThat(first.getContent()).hasSize(2);
        assertThat(second.getContent()).hasSize(2);
        assertThat(first.getContent()).doesNotContainAnyElementsOf(second.getContent());
        assertThat(first.getContent())
                .allSatisfy(order -> assertThat(order.getRetailer().getId())
                        .isEqualTo(retailer.getId()));
    }

    @Test
    @DisplayName("sorting behaves the same scoped as unscoped")
    void sortingMatchesUnscopedBehaviour() {
        Page<Order> newestFirst = orderService.getOrders(
                retailerFilter(null), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "orderDate")));
        Page<Order> oldestFirst = orderService.getOrders(
                retailerFilter(null), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "orderDate")));

        assertThat(newestFirst.getContent())
                .isSortedAccordingTo((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()));
        assertThat(oldestFirst.getContent())
                .isSortedAccordingTo((a, b) -> a.getOrderDate().compareTo(b.getOrderDate()));
    }

    @Test
    @DisplayName("the unscoped query still sees every retailer's orders")
    void unscopedQuerySeesEverything() {
        Page<Order> all = orderService.getOrders(
                new OrderFilter(null, null, null, null, null, null), unpaged());

        assertThat(all.getTotalElements()).isEqualTo(OrderStatus.values().length + 2);
    }

    private OrderFilter retailerFilter(List<OrderStatus> statuses) {
        return new OrderFilter(null, statuses, null, null, retailer.getId(), null);
    }

    private PageRequest unpaged() {
        return PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    private Retailer retailer(String code, String storeName, String phone, Employee salesman) {
        return Retailer.builder()
                .code(code)
                .storeName(storeName)
                .contactPerson("Owner " + code)
                .phone(phone)
                .passwordHash("hash")
                .assignedSalesman(salesman)
                .joinedOn(LocalDate.now())
                .build();
    }

    private Order order(Retailer owner, Employee salesman, String code, OrderStatus status, int day) {
        return Order.builder()
                .code(code)
                .retailer(owner)
                .handledBy(salesman)
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
