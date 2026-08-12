package com.inaing.blackhorse_erp.module.backlog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.backlog.domain.Backlog;

public interface BacklogRepository extends JpaRepository<Backlog, String> {

  boolean existsByFactoryId(String id);

  Optional<Backlog> findByFactoryId(String factoryId);

}