package com.inaing.blackhorse_erp.module.factory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.factory.domain.Factory;

public interface FactoryRepository extends JpaRepository<Factory, String> {

    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<Factory> findByCode(String code);

    Optional<Factory> findByName(String name);

    Optional<Factory> findByManagerId(String managerId);

}
