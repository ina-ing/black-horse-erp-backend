package com.inaing.blackhorse_erp.module.retailer.dto.request;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessMedium;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RetailerUpdateRequestDto(
        String assignedSalesman,
        @Size(max = 120) String storeName,
        String contactPerson,
        @Email @Size(max = 160) String email,
        @Pattern(regexp = "\\+?\\d{7,15}", message = "must be a valid phone number") String phone,
        BusinessType businessType,
        BusinessMedium businessMedium,
        @Size(max = 10) String panNumber,
        Province province,
        @Size(max = 200) String storeAddress) {
}
