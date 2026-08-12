package com.inaing.blackhorse_erp.module.supply.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;

public interface SupplyRepository extends JpaRepository<Supply, String> {

    boolean existsByCode(String code);

    Optional<Supply> findByCode(String code);
}
