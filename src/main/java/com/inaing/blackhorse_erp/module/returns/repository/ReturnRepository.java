package com.inaing.blackhorse_erp.module.returns.repository;

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

import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.projections.ReturnStatusCountProjection;

public interface ReturnRepository extends JpaRepository<Return, String>, JpaSpecificationExecutor<Return> {

    boolean existsByCode(String code);

    @Override
    @EntityGraph(attributePaths = { "retailer", "handledBy" })
    Page<Return> findAll(Specification<Return> spec, Pageable pageable);

    // Counts ignore the list filters but honour the caller's role scope, so the
    // stat cards stay stable while the table is filtered.
    @Query("""
            SELECT r.status AS status, COUNT(r) AS count
            FROM Return r
            WHERE (:handledById IS NULL OR r.handledBy.id = :handledById)
              AND (:retailerId IS NULL OR r.retailer.id = :retailerId)
              AND r.reason IN :reasons
            GROUP BY r.status
            """)
    List<ReturnStatusCountProjection> findStatusCounts(
            @Param("handledById") String handledById,
            @Param("retailerId") String retailerId,
            @Param("reasons") List<ReturnReason> reasons);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Return> findByCode(String code);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Return> findById(String id);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    List<Return> findAllByStatus(ReturnStatus status);
}