package com.inaing.blackhorse_erp.module.productionOrder.domain;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "production_order_item", uniqueConstraints = @UniqueConstraint(columnNames = { "production_order_id",
        "variant_size_id" }))
@SQLRestriction("deleted = false")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductionOrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_size_id", nullable = false)
    private ProductVariantSize variantSize;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
