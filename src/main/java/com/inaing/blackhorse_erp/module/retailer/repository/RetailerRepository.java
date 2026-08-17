package com.inaing.blackhorse_erp.module.retailer.repository;

import java.time.LocalDate;
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

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;

public interface RetailerRepository
        extends JpaRepository<Retailer, String>, JpaSpecificationExecutor<Retailer> {

    @Override
    @EntityGraph(attributePaths = "assignedSalesman")
    Page<Retailer> findAll(Specification<Retailer> spec, Pageable pageable);


    @EntityGraph(attributePaths = { "assignedSalesman" })
    Optional<Retailer> findByPhone(String phone);

    @EntityGraph(attributePaths = { "assignedSalesman" })
    Optional<Retailer> findByCode(String code);

    boolean existsByPhone(String phone);

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "assignedSalesman" })
    List<Retailer> findByAssignedSalesmanId(String salesmanId);

    long countByJoinedOnBetween(LocalDate start, LocalDate end);

    long countByAssignedSalesmanId(String salesmanId);

    // Distinct provinces the retailers sit in, optionally for one salesman only.
    @Query("""
            SELECT COUNT(DISTINCT r.province)
            FROM Retailer r
            WHERE (:assignedSalesmanId IS NULL OR r.assignedSalesman.id = :assignedSalesmanId)
            """)
    long countDistinctProvinces(@Param("assignedSalesmanId") String assignedSalesmanId);

    long countByAssignedSalesmanIdAndJoinedOnBetween(String salesmanId, LocalDate start, LocalDate end);
}
