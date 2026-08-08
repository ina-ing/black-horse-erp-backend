package com.inaing.blackhorse_erp.module.backlog.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "backlogs", uniqueConstraints = @UniqueConstraint(columnNames = "factory_id"))
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Backlog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factory_id", nullable = false, unique = true)
    private Factory factory;

    @Column(name = "total_quantity", nullable = false)
    @Builder.Default
    private Integer totalQuantity = 0;

    @Column(name = "total_articles", nullable = false)
    @Builder.Default
    private Integer totalArticles = 0;

    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BacklogItem> items = new ArrayList<>();

    public void addQuantity(ProductVariantSize variantSize, int quantity) {
        if (quantity <= 0) {
            return;
        }

        findItem(variantSize).ifPresentOrElse(
                item -> item.setQuantity(item.getQuantity() + quantity),
                () -> items.add(BacklogItem.builder()
                        .backlog(this)
                        .variantSize(variantSize)
                        .quantity(quantity)
                        .build()));
    }

    public void reduce(ProductVariantSize variantSize, int quantity) {
        if (quantity <= 0) {
            return;
        }
        findItem(variantSize).ifPresent(
                item -> item.setQuantity(Math.max(0, item.getQuantity() - quantity)));
    }

    private Optional<BacklogItem> findItem(ProductVariantSize variantSize) {
        return items.stream()
                .filter(item -> item.getVariantSize().getId().equals(variantSize.getId()))
                .findFirst();
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
