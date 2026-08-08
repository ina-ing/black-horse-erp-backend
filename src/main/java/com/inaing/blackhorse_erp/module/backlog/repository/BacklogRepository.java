package com.inaing.blackhorse_erp.module.backlog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inaing.blackhorse_erp.module.backlog.domain.Backlog;

public interface BacklogRepository extends JpaRepository<Backlog, String> {

    Optional<Backlog> findByFactoryId(String factoryId);

    @Query("""
            select b from ProductionBacklog b
            join fetch b.items i
            where b.factory.id = :factoryId
              and i.quantity > 0
            """)
    Optional<Backlog> findWithOutstandingItems(String factoryId);
}   