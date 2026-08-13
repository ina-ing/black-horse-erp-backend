package com.inaing.blackhorse_erp.module.factory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.factory.domain.Factory;

public interface FactoryRepository extends JpaRepository<Factory, String> {

    boolean existsByName(String name);

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "manager" })
    Optional<Factory> findByCode(String code);

    @EntityGraph(attributePaths = { "manager" })
    Optional<Factory> findByName(String name);

    @EntityGraph(attributePaths = { "manager" })
    Optional<Factory> findByManagerId(String managerId);

}
