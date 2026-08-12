package com.inaing.blackhorse_erp.module.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {

}
