package com.inaing.blackhorse_erp.module.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.common.dto.response.ContactResponseDto;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderItem;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderItemsResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderListResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "retailer", source = "retailer.storeName")
    @Mapping(target = "handledBy", source = "handledBy.fullname")
    OrderResponseDto toResponse(Order order);

    @Mapping(target = "retailer", source = "retailer.storeName")
    @Mapping(target = "handledBy", source = "handledBy.fullname")
    OrderListResponseDto toListResponse(Order order);

    @Mapping(target = "retailer", source = "order.retailer.storeName")
    @Mapping(target = "handledBy", source = "order.handledBy.fullname")
    @Mapping(target = "retailerDetails", source = "order.retailer")
    @Mapping(target = "handledByDetails", source = "order.handledBy")
    OrderWithStatusHistoryResponseDto toResponseWithHistory(Order order,
            List<OrderStatusHistoryResponseDto> statusHistory);

    @Mapping(target = "fullname", source = "storeName")
    @Mapping(target = "address", source = "storeAddress")
    ContactResponseDto toPartyContact(Retailer retailer);

    ContactResponseDto toPartyContact(Employee employee);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    @Mapping(target = "articleCode", source = "variantSize.productVariant.product.articleCode")
    OrderItemsResponseDto toItemResponse(OrderItem item);

    OrderStatusHistoryResponseDto toHistoryResponse(OrderStatusHistory history);

    List<OrderStatusHistoryResponseDto> toHistoryResponseList(List<OrderStatusHistory> histories);
}
