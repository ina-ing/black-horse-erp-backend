package com.inaing.blackhorse_erp.module.order.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.projections.OrderMonthlyVolumeProjection;
import com.inaing.blackhorse_erp.module.order.dto.projections.OrderStatusCountProjection;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    boolean existsByCode(String code);

    @Override
    @EntityGraph(attributePaths = { "retailer", "handledBy" })
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "retailer", "handledBy" })
    Optional<Order> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "retailer", "handledBy" })
    Optional<Order> findById(String id);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "retailer", "handledBy" })
    List<Order> findAllBy();

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "retailer", "handledBy" })
    List<Order> findAllByStatus(OrderStatus status);

    // Counts ignore the list filters but honour the caller's role scope, so the
    // stat cards stay stable while the table is filtered.
    @Query("""
            SELECT o.status AS status, COUNT(o) AS count
            FROM Order o
            WHERE (:handledById IS NULL OR o.handledBy.id = :handledById)
              AND (:retailerId IS NULL OR o.retailer.id = :retailerId)
              AND o.status IN :statuses
            GROUP BY o.status
            """)
    List<OrderStatusCountProjection> findStatusCounts(
            @Param("handledById") String handledById,
            @Param("retailerId") String retailerId,
            @Param("statuses") List<OrderStatus> statuses);

    // One row per month that saw orders; quiet months are filled in by the use case.
    @Query("""
            SELECT YEAR(o.orderDate) AS year,
                   MONTH(o.orderDate) AS month,
                   COALESCE(SUM(o.totalQuantity), 0) AS quantity
            FROM Order o
            WHERE o.orderDate >= :from
              AND (:handledById IS NULL OR o.handledBy.id = :handledById)
              AND (:retailerId IS NULL OR o.retailer.id = :retailerId)
              AND o.status IN :statuses
            GROUP BY YEAR(o.orderDate), MONTH(o.orderDate)
            ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)
            """)
    List<OrderMonthlyVolumeProjection> findMonthlyVolume(
            @Param("from") Instant from,
            @Param("handledById") String handledById,
            @Param("retailerId") String retailerId,
            @Param("statuses") List<OrderStatus> statuses);
}
