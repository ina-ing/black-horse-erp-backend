package com.inaing.blackhorse_erp.module.returns.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "returns")
@SQLRestriction("deleted = false")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Return extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "retailer_id", nullable = false)
    private Retailer retailer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "handled_by", nullable = false)
    private Employee handledBy;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "total_quantity", nullable = false)
    @Builder.Default
    private Integer totalQuantity = 0;

    @Column(name = "total_articles", nullable = false)
    @Builder.Default
    private Integer totalArticles = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReturnReason reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReturnStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "created_by_role", nullable = false, updatable = false)
    private Role createdByRole;

    @Column(name = "return_date", nullable = false)
    @Builder.Default
    private Instant returnDate = Instant.now();

    @Column(name = "note", columnDefinition = "TEXT", nullable = true)
    private String note;

    @OneToMany(mappedBy = "returnId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReturnItem> items = new ArrayList<>();

    public void addItem(ReturnItem item) {
        items.add(item);
        item.setReturnId(this);
    }

    public void removeItem(ReturnItem item) {
        items.remove(item);
        item.setReturnId(null);
    }

    public void recalculateTotals() {
        this.totalArticles = (int) items.stream()
                .filter(i -> i.getQuantity() > 0)
                .count();
        this.totalQuantity = items.stream()
                .mapToInt(item -> item.getQuantity())
                .sum();
    }
}
