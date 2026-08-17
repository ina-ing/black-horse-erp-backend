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
 * Admin reads the whole book: every retailer, every salesman, every status.
 */
@DataJpaTest
@Import(OrderServiceImpl.class)
class OrderAdminScopeTest {

    private static final int TOTAL_ORDERS = 8;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void seed() {
        Employee salesman = persist(employee("EMP-001", "Ram Bahadur", "9800000001"));
        Employee otherSalesman = persist(employee("EMP-002", "Hari Shrestha", "9800000002"));

        Retailer first = persist(retailer("RET-001", "ABC Mart", "9811111111", salesman));
        Retailer second = persist(retailer("RET-002", "Hill Top Traders", "9822222222", otherSalesman));

        int day = 1;
        for (OrderStatus status : OrderStatus.values()) {
            persist(order(first, salesman, "ORD-1" + day, status, day));
            day++;
        }

        persist(order(second, otherSalesman, "ORD-901", OrderStatus.PENDING, 1));
        persist(order(second, otherSalesman, "ORD-902", OrderStatus.CANCELLED, 2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("admin sees every retailer's and every salesman's orders")
    void seesEverything() {
        Page<Order> page = orderService.getOrders(adminFilter(null), unpaged());

        assertThat(page.getTotalElements()).isEqualTo(TOTAL_ORDERS);
        assertThat(page.getContent())
                .extracting(order -> order.getRetailer().getCode())
                .contains("RET-001", "RET-002");
        assertThat(page.getContent())
                .extracting(order -> order.getHandledBy().getCode())
                .contains("EMP-001", "EMP-002");
    }

    @Test
    @DisplayName("admin sees every status, cancelled included")
    void seesEveryStatus() {
        Page<Order> page = orderService.getOrders(adminFilter(null), unpaged());

        assertThat(page.getContent())
                .extracting(Order::getStatus)
                .containsAll(List.of(OrderStatus.values()));
    }

    @Test
    @DisplayName("admin can narrow to one retailer without being scoped to it")
    void canNarrowByRetailer() {
        Page<Order> narrowed = orderService.getOrders(
                new OrderFilter(null, null, null, null, null, "RET-002"), unpaged());

        assertThat(narrowed.getTotalElements()).isEqualTo(2);
        assertThat(narrowed.getContent())
                .allSatisfy(order -> assertThat(order.getRetailer().getCode()).isEqualTo("RET-002"));
    }

    @Test
    @DisplayName("paging counts the whole book")
    void pagingCoversEverything() {
        Sort byDate = Sort.by(Sort.Direction.DESC, "orderDate");

        Page<Order> first = orderService.getOrders(adminFilter(null), PageRequest.of(0, 5, byDate));
        Page<Order> second = orderService.getOrders(adminFilter(null), PageRequest.of(1, 5, byDate));

        assertThat(first.getTotalElements()).isEqualTo(TOTAL_ORDERS);
        assertThat(first.getContent()).hasSize(5);
        assertThat(second.getContent()).hasSize(TOTAL_ORDERS - 5);
        assertThat(first.getContent()).doesNotContainAnyElementsOf(second.getContent());
    }

    private OrderFilter adminFilter(List<OrderStatus> statuses) {
        return new OrderFilter(null, statuses, null, null, null, null);
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
