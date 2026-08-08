package com.inaing.blackhorse_erp.module.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseCreationRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseUpdateRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.usecase.IWarehouseUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final IWarehouseUsecases warehouseUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<WarehouseResponseDto> create(@Valid @RequestBody WarehouseCreationRequestDto respose) {
        return ApiResponse.created("Warehouse created", warehouseUsecases.create(respose));
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<WarehouseResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody WarehouseUpdateRequestDto request) {
        return ApiResponse.ok("Warehouse updated", warehouseUsecases.update(identifier, request));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<WarehouseResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(warehouseUsecases.getByIdentifier(identifier));
    }

}
