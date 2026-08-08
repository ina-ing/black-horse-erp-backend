package com.inaing.blackhorse_erp.module.warehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<Warehouse> findByCode(String identifier);

    Optional<Warehouse> findByName(String name);
}
