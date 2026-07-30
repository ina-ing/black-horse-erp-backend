package com.inaing.blackhorse_erp.module.retailer.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;

public interface RetailerRepository extends JpaRepository<Retailer, String> {

    Optional<Retailer> findByPhone(String phone);

    Optional<Retailer> findByCode(String code);

    boolean existsByPhone(String phone);

    boolean existsByCode(String code);

    long countByJoinedOnBetween(LocalDate start, LocalDate end);
}
