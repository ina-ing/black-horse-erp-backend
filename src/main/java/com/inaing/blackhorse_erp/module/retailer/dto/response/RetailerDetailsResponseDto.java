package com.inaing.blackhorse_erp.module.retailer.dto.response;

import java.time.LocalDate;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.common.dto.response.ContactResponseDto;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessMedium;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

public record RetailerDetailsResponseDto(
        String id,
        String code,
        String storeName,
        String contactPerson,
        String phone,
        String email,
        String panNumber,
        String storeAddress,
        Province province,
        BusinessType businessType,
        BusinessMedium businessMedium,
        ContactResponseDto assignedSalesman,
        LocalDate joinedOn) {

}
