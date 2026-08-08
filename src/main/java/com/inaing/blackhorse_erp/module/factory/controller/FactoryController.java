package com.inaing.blackhorse_erp.module.factory.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;
import com.inaing.blackhorse_erp.module.factory.usecase.IFactoryUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/factory")
@RequiredArgsConstructor
public class FactoryController {

    private final IFactoryUsecases factoryUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<FactoryResponseDto> create(@Valid @RequestBody FactoryRequestDto request) {
        return ApiResponse.created("Factory created", factoryUsecases.create(request));
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<FactoryResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody FactoryRequestDto request) {
        return ApiResponse.ok("Factory updated", factoryUsecases.update(identifier, request));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<FactoryResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(factoryUsecases.getByIdentifier(identifier));
    }

}
