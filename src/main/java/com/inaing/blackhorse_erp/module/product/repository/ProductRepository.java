package com.inaing.blackhorse_erp.module.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inaing.blackhorse_erp.module.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByArticleCode(String articleCode);

    boolean existsByArticleCode(String articleCode);

    @Query("""
            select distinct p from Product p
            left join fetch p.variants v
            left join fetch v.sizes
            where p.id = :id
            """)
    Optional<Product> findByIdWithVariantsAndSizes(String id);

    @EntityGraph(attributePaths = { "variants", "variants.sizes" })
    List<Product> findAllBy();
}
