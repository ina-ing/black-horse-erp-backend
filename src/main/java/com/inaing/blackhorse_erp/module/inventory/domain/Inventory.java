package com.inaing.blackhorse_erp.module.inventory.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventories")
@SQLRestriction("deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Inventory extends BaseEntity {     

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false)
    private LocationType locationType;

    @Column(name = "reference_id", unique = true, nullable = false)
    private String reference;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryItem> items = new ArrayList<>();
 
    /**
     * Lazily-built, per-instance lookup cache keyed by variant size id.
     * Never persisted (@Transient) — rebuilt once on first access after
     * this entity instance is loaded, then kept in sync in-place as items
     * are added. Avoids rebuilding an index on every add/reduce call.
     */
    @Transient
    private Map<String, InventoryItem> itemIndex;

    public void addQuantity(ProductVariantSize variantSize, int quantity) {
        addQuantities(Map.of(variantSize, quantity));
    }

    public void reduce(ProductVariantSize variantSize, int quantity) {
        reduceQuantities(Map.of(variantSize, quantity));
    }

    public void addQuantities(Map<ProductVariantSize, Integer> quantitiesByVariant) {
        Map<String, InventoryItem> index = index();

        quantitiesByVariant.forEach((variantSize, quantity) -> {
            if (quantity <= 0) {
                return;
            }

            InventoryItem item = index.get(variantSize.getId());
            if (item != null) {
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                InventoryItem newItem = InventoryItem.builder()
                        .inventory(this)
                        .variantSize(variantSize)
                        .quantity(quantity)
                        .build();
                items.add(newItem);
                index.put(variantSize.getId(), newItem);
            }

        });
    }

    public void setQuantities(Map<ProductVariantSize, Integer> quantitiesByVariant) {
        Map<String, InventoryItem> index = index();

        quantitiesByVariant.forEach((variantSize, quantity) -> {
            InventoryItem item = index.get(variantSize.getId());
            if (item != null) {
                item.setQuantity(quantity);
            } else {
                InventoryItem newItem = InventoryItem.builder()
                        .inventory(this)
                        .variantSize(variantSize)
                        .quantity(quantity)
                        .build();
                items.add(newItem);
                index.put(variantSize.getId(), newItem);
            }
        });
    }

    public void reduceQuantities(Map<ProductVariantSize, Integer> quantitiesByVariant) {
        Map<String, InventoryItem> index = index();

        Map<InventoryItem, Integer> resolved = new HashMap<>();
        quantitiesByVariant.forEach((variantSize, quantity) -> {
            if (quantity <= 0) {
                return;
            }

            InventoryItem item = index.get(variantSize.getId());
            if (item == null) {
                throw new AppException(ErrorCode.INVENTORY_ITEM_NOT_FOUND,
                        "Inventory item not found: " + variantSize.getSku());
            }
            assertSufficientStock(item, variantSize, quantity);
            resolved.put(item, quantity);
        });

        resolved.forEach((item, quantity) -> {
            item.setQuantity(item.getQuantity() - quantity);
        });
    }

    private void assertSufficientStock(InventoryItem item, ProductVariantSize variantSize, int quantity) {
        if (item.getQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK,
                    "Insufficient stock for variant size %s: requested %d, available %d"
                            .formatted(variantSize.getSku(), quantity, item.getQuantity()));
        }
    }

    private Map<String, InventoryItem> index() {
        if (itemIndex == null) {
            itemIndex = new HashMap<>();
            for (InventoryItem item : items) {
                itemIndex.put(item.getVariantSize().getId(), item);
            }
        }
        return itemIndex;
    }
}