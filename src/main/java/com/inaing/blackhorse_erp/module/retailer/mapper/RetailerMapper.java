package com.inaing.blackhorse_erp.module.retailer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;

@Mapper(componentModel = "spring")
public interface RetailerMapper {

    RetailerResponseDto toResponse(Retailer retailer);

    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "assignedSalesman", ignore = true)
    Retailer toEntity(RetailerCreationRequestDto request);
}