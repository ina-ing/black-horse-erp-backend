package com.inaing.blackhorse_erp.module.supply.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;

public interface SupplyRepository extends JpaRepository<Supply, String> {

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    Optional<Supply> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    Optional<Supply> findById(String id);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "suppliedBy", "suppliedTo" })
    List<Supply> findAll();
}
