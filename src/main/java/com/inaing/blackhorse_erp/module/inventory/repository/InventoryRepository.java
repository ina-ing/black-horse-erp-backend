package com.inaing.blackhorse_erp.module.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;

public interface InventoryRepository extends JpaRepository<Inventory, String> {

    boolean existsByReference(String id);

    Optional<Inventory> findByLocationTypeAndReference(LocationType locationType, String reference);

    boolean existsByLocationTypeAndReference(LocationType locationType, String reference);

}
