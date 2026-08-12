package com.inaing.blackhorse_erp.module.retailer.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerUpdateRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;

@Mapper(componentModel = "spring")
public interface RetailerMapper {

    RetailerResponseDto toResponse(Retailer retailer);

    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "assignedSalesman", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "joinedOn", ignore = true)
    Retailer toEntity(RetailerCreationRequestDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignedSalesman", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "joinedOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(RetailerUpdateRequestDto request, @MappingTarget Retailer retailer);
}