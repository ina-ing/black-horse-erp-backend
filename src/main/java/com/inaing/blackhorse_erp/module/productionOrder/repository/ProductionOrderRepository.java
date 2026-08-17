package com.inaing.blackhorse_erp.module.productionOrder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.dto.projections.ProductionOrderAnalyticsProjection;

public interface ProductionOrderRepository
        extends JpaRepository<ProductionOrder, String>, JpaSpecificationExecutor<ProductionOrder> {

    boolean existsByCode(String code);

    // The list never renders items, so only the warehouse is fetched with the page.
    @Override
    @EntityGraph(attributePaths = "warehouse")
    Page<ProductionOrder> findAll(Specification<ProductionOrder> spec, Pageable pageable);

    // Counts and pairs per status; the use case totals them for the stat cards.
    @Query("""
            SELECT o.status AS status,
                   COUNT(o) AS count,
                   COALESCE(SUM(o.totalQuantity), 0) AS quantity
            FROM ProductionOrder o
            GROUP BY o.status
            """)
    List<ProductionOrderAnalyticsProjection> findAnalytics();

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    Optional<ProductionOrder> findById(String id);
  
    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    Optional<ProductionOrder> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    List<ProductionOrder> findAll();
}
