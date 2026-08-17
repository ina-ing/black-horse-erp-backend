package com.inaing.blackhorse_erp.module.activityLog.domain;

import java.time.Instant;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityPriority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activity_logs", indexes = {
        @Index(name = "idx_activity_log_occurred_at", columnList = "occurred_at"),
        @Index(name = "idx_activity_log_priority", columnList = "priority"),
        @Index(name = "idx_activity_log_actor", columnList = "actor_id"),
        @Index(name = "idx_activity_log_entity", columnList = "entity_type, entity_id")
})
@SQLRestriction("deleted = false")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ActivityLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 60)
    private ActivityAction action;

    @Column(name = "details", nullable = false, length = 512)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 10)
    private ActivityPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false, length = 20)
    private ActionTrigger trigger;

    @Column(name = "actor_id", length = 36)
    private String actorId;

    @Column(name = "actor_name", nullable = false, length = 120)
    private String actorName;

    @Column(name = "actor_role", nullable = false, length = 30)
    private String actorRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", length = 30)
    private ActivityEntityType entityType;

    @Column(name = "entity_id", length = 36)
    private String entityId;

    @Column(name = "entity_code", length = 30)
    private String entityCode;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;
}
