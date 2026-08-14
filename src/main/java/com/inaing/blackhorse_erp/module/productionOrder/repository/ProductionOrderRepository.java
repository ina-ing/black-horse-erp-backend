package com.inaing.blackhorse_erp.module.productionOrder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, String> {

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    Optional<ProductionOrder> findById(String id);
  
    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    Optional<ProductionOrder> findByCode(String code);

    @EntityGraph(attributePaths = { "items.variantSize.productVariant.product", "warehouse" })
    List<ProductionOrder> findAll();
}
