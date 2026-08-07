package com.inaing.blackhorse_erp.module.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseCreationRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseUpdateRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "manager", source = "manager.fullname")
    @Mapping(target = "phone", source = "manager.phone")
    @Mapping(target = "email", source = "manager.email")
    WarehouseResponseDto toResponse(Warehouse warehouse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "manager", ignore = true)
    Warehouse toEntity(WarehouseCreationRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "manager", ignore = true)
    void updateEntity(WarehouseUpdateRequestDto request, @MappingTarget Warehouse warehouse);
}
