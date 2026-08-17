package com.inaing.blackhorse_erp.module.production.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.dto.projections.ProductionTotalsProjection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductionRepository
        extends JpaRepository<Production, String>, JpaSpecificationExecutor<Production> {

    boolean existsByCode(String code);

    // The list never renders items, so only the factory is fetched with the page.
    @Override
    @EntityGraph(attributePaths = "factory")
    Page<Production> findAll(Specification<Production> spec, Pageable pageable);

    // Totals ignore the list filters, so the stat cards stay stable while filtering.
    @Query("""
            SELECT COUNT(p) AS total,
                   COALESCE(SUM(p.totalQuantity), 0) AS totalQuantity,
                   COALESCE(SUM(p.totalArticles), 0) AS totalArticles
            FROM Production p
            """)
    ProductionTotalsProjection findTotals();

    long countByProductionDateGreaterThanEqual(Instant from);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    Optional<Production> findById(String id);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    Optional<Production> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    List<Production> findAll();

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    List<Production> findByFactoryId(String factoryId);
}
