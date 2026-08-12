package com.inaing.blackhorse_erp.module.inventory.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;
import com.inaing.blackhorse_erp.module.inventory.usecase.IInventoryUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryUsecases inventoryUsecases;

    @GetMapping("/{locationType}/{referenceId}")
    public ApiResponse<InventoryResponseDto> getByLocation(@PathVariable LocationType locationType,
            @PathVariable String referenceId) {
        return ApiResponse.ok(inventoryUsecases.getByLocation(locationType, referenceId));
    }

    @PutMapping("/adjust")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<InventoryResponseDto> adjust(@Valid @RequestBody InventoryAdjustmentRequestDto request) {
        return ApiResponse.ok("Inventory adjusted", inventoryUsecases.adjust(request));
    }
}
