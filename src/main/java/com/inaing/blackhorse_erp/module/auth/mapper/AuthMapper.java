package com.inaing.blackhorse_erp.module.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.auth.dto.employee.EmployeeLoginResponseDto;
import com.inaing.blackhorse_erp.module.auth.dto.retailer.RetailerLoginResponseDto;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "name", source = "fullname")
    EmployeeLoginResponseDto toEmployeeLoginResponseDto(Employee employee);

    @Mapping(target = "role", constant = "CUSTOMER")
    RetailerLoginResponseDto toRetailerLoginResponseDto(Retailer retailer);
}
