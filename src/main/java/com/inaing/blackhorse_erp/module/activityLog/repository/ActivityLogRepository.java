package com.inaing.blackhorse_erp.module.activityLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.inaing.blackhorse_erp.module.activityLog.domain.ActivityLog;

@Repository
public interface ActivityLogRepository
        extends JpaRepository<ActivityLog, String>, JpaSpecificationExecutor<ActivityLog> {
}
