package com.inaing.blackhorse_erp.module.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    boolean existsByIdentifier(String identifier);

    Optional<Category> findByNameIgnoreCaseOrIdentifierIgnoreCase(
            String name,
            String identifier);
}
