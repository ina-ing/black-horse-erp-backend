package com.inaing.blackhorse_erp.module.retailer.dto.request;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessMedium;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RetailerCreationRequestDto(
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank String assignedSalesman,
        @NotBlank @Size(max = 120) String storeName,
        @NotBlank String contactPerson,
        @Email @Size(max = 160) String email,
        @NotBlank @Pattern(regexp = "\\+?\\d{7,15}", message = "must be a valid phone number") String phone,
        @NotNull BusinessType businessType,
        @NotNull BusinessMedium businessMedium,
        @Size(max = 10) @NotBlank String panNumber,
        @NotNull Province province,
        @NotBlank @Size(max = 200) String storeAddress) {
}
