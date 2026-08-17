package com.inaing.blackhorse_erp.module.inventory.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryItemResponseDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;
import com.inaing.blackhorse_erp.module.inventory.usecase.IInventoryUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryUsecases inventoryUsecases;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES','WAREHOUSE','FACTORY')")
    public ApiResponse<InventoryResponseDto> getByLocation(@RequestParam LocationType locationType,
            @RequestParam String referenceId) {
        return ApiResponse.ok(inventoryUsecases.getByLocation(locationType, referenceId));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN','SALES','WAREHOUSE','FACTORY')")
    public ApiResponse<List<InventoryItemResponseDto>> getByLocationAndProduct(
            @RequestParam LocationType locationType,
            @RequestParam String referenceId,
            @RequestParam String productId) {
        return ApiResponse.ok(inventoryUsecases.getItemsByLocationAndProduct(locationType, referenceId, productId));
    }

    @PutMapping("/adjust")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<InventoryResponseDto> adjust(@Valid @RequestBody InventoryAdjustmentRequestDto request) {
        return ApiResponse.ok("Inventory adjusted", inventoryUsecases.adjust(request));
    }
}
