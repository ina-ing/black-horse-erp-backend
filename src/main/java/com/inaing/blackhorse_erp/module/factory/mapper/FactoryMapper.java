package com.inaing.blackhorse_erp.module.factory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;

@Mapper(componentModel = "spring")
public interface FactoryMapper {

    @Mapping(target = "manager", source = "manager.fullname")
    @Mapping(target = "phone", source = "manager.phone")
    @Mapping(target = "email", source = "manager.email")
    FactoryResponseDto toResponse(Factory factory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "manager", ignore = true)
    Factory toEntity(FactoryRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "manager", ignore = true)
    void updateEntity(FactoryRequestDto request, @MappingTarget Factory factory);
}
