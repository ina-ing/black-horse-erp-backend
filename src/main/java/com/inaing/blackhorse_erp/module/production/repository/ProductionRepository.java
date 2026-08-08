package com.inaing.blackhorse_erp.module.production.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.production.domain.Production;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, String> {

    boolean existsByCode(String code);

    Optional<Production> findByCode(String code);
}
