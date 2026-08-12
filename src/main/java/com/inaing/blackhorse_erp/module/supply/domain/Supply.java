package com.inaing.blackhorse_erp.module.supply.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;

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
@Table(name = "supplies")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Supply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplied_by", nullable = false)
    private Factory suppliedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplied_to", nullable = false)
    private Warehouse suppliedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SupplyStatus status = SupplyStatus.PENDING;

    @Column(name = "total_quantity", nullable = false)
    @Builder.Default
    private Integer totalQuantity = 0;

    @Column(name = "total_articles", nullable = false)
    @Builder.Default
    private Integer totalArticles = 0;

    @Column(name = "supply_date", nullable = false)
    @Builder.Default
    private Instant supplyDate = Instant.now();

    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SupplyItem> items = new ArrayList<>();

    public void addItem(SupplyItem item) {
        items.add(item);
        item.setSupply(this);
    }

    public void removeItem(SupplyItem item) {
        items.remove(item);
        item.setSupply(null);
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
