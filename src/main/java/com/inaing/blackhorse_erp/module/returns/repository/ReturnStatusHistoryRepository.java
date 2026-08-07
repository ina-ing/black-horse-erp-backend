package com.inaing.blackhorse_erp.module.returns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.returns.domain.ReturnStatusHistory;

public interface ReturnStatusHistoryRepository extends JpaRepository<ReturnStatusHistory, String> {

    List<ReturnStatusHistory> findByReturnIdIdOrderByCreatedAtAsc(String returnId);
}
