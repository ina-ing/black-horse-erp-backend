package com.inaing.blackhorse_erp.module.supply.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.dto.projections.SupplyStatusCountProjection;

public interface SupplyRepository extends JpaRepository<Supply, String>, JpaSpecificationExecutor<Supply> {

    boolean existsByCode(String code);

    // The list never renders items, so only the two facilities are fetched with the page.
    @Override
    @EntityGraph(attributePaths = { "suppliedBy", "suppliedTo" })
    Page<Supply> findAll(Specification<Supply> spec, Pageable pageable);

    // Counts ignore the list filters, so the stat cards stay stable while filtering.
    @Query("""
            SELECT s.status AS status, COUNT(s) AS count
            FROM Supply s
            GROUP BY s.status
            """)
    List<SupplyStatusCountProjection> findStatusCounts();

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    Optional<Supply> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    Optional<Supply> findById(String id);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    List<Supply> findAll();
}
