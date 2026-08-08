package com.inaing.blackhorse_erp.module.productionOrder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, String>{

    boolean existsByCode(String code);

    Optional<ProductionOrder> findByCode(String code);
}
