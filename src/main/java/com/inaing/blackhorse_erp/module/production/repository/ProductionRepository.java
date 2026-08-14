package com.inaing.blackhorse_erp.module.production.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.production.domain.Production;

import java.util.List;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, String> {

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    Optional<Production> findById(String id);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    Optional<Production> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    List<Production> findAll();

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "factory" })
    List<Production> findByFactoryId(String factoryId);
}
