package com.inaing.blackhorse_erp.module.retailer.dto.request;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetailerQueryParams extends PageableRequest {

    private Province province;

    private BusinessType businessType;
}
