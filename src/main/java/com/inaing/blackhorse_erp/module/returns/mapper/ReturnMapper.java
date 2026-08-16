package com.inaing.blackhorse_erp.module.returns.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.common.dto.response.ContactResponseDto;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnItem;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnStatusHistory;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnItemsResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnListResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;

@Mapper(componentModel = "spring")
public interface ReturnMapper {

    @Mapping(target = "retailer", source = "retailer.storeName")
    @Mapping(target = "handledBy", source = "handledBy.fullname")
    ReturnResponseDto toResponse(Return ret);

    @Mapping(target = "retailer", source = "retailer.storeName")
    @Mapping(target = "handledBy", source = "handledBy.fullname")
    ReturnListResponseDto toListResponse(Return ret);

    @Mapping(target = "retailer", source = "ret.retailer.storeName")
    @Mapping(target = "handledBy", source = "ret.handledBy.fullname")
    @Mapping(target = "retailerDetails", source = "ret.retailer")
    @Mapping(target = "handledByDetails", source = "ret.handledBy")
    ReturnWithStatusHistoryResponseDto toResponseWithHistory(Return ret,
            List<ReturnStatusHistoryResponseDto> statusHistory);

    @Mapping(target = "fullname", source = "storeName")
    @Mapping(target = "address", source = "storeAddress")
    ContactResponseDto toPartyContact(Retailer retailer);

    ContactResponseDto toPartyContact(Employee employee);

    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    @Mapping(target = "articleCode", source = "variantSize.productVariant.product.articleCode")
    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    ReturnItemsResponseDto toItemResponse(ReturnItem item);

    ReturnStatusHistoryResponseDto toHistoryResponse(ReturnStatusHistory history);

    List<ReturnStatusHistoryResponseDto> toHistoryResponseList(List<ReturnStatusHistory> histories);
}
