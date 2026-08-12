package com.inaing.blackhorse_erp.module.production.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productions", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Production extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factory_id", nullable = false)
    private Factory factory;

    @Column(name = "total_quantity", nullable = false)
    @Builder.Default
    private Integer totalQuantity = 0;

    @Column(name = "total_articles", nullable = false)
    @Builder.Default
    private Integer totalArticles = 0;

    @Column(name = "production_date", nullable = false)
    @Builder.Default
    private Instant productionDate = Instant.now();

    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductionItem> items = new ArrayList<>();

    public void addItem(ProductionItem item) {
        items.add(item);
        item.setProduction(this);
    }

    public void removeItem(ProductionItem item){
        items.remove(item);
        item.setProduction(null);
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
