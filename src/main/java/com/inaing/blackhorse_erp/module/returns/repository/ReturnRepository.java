package com.inaing.blackhorse_erp.module.returns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.returns.domain.Return;

public interface ReturnRepository extends JpaRepository<Return, String> {

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Return> findByCode(String code);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Return> findById(String id);
}