package com.inaing.blackhorse_erp.module.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

    boolean existsByCode(String code);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Order> findByCode(String code);

    @EntityGraph(attributePaths = { "items", "items.variantSize", "retailer", "handledBy" })
    Optional<Order> findById(String id);

     @EntityGraph(attributePaths = {"items", "items.variantSize", "retailer", "handledBy"})
    List<Order> findAllBy();
}
