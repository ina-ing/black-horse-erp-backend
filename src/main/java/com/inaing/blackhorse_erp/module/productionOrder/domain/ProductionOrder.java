package com.inaing.blackhorse_erp.module.productionOrder.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;
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
@Table(name = "production_orders")
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductionOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "total_quantity", nullable = false)
    @Builder.Default
    private Integer totalQuantity = 0;

    @Column(name = "total_articles", nullable = false)
    @Builder.Default
    private Integer totalArticles = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ProductionOrderStatus status = ProductionOrderStatus.PENDING;

    @Column(name = "order_date", nullable = false)
    @Builder.Default
    private Instant orderDate = Instant.now();

    @OneToMany(mappedBy = "productionOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductionOrderItem> items = new ArrayList<>();

    public void addItem(ProductionOrderItem item) {
        items.add(item);
        item.setProductionOrder(this);
    }

    public void removeItem(ProductionOrderItem item) {
        items.remove(item);
        item.setProductionOrder(null);
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
