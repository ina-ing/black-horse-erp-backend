package com.inaing.blackhorse_erp.module.retailer.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerFilter;

public interface IRetailerService {

    Retailer create(Retailer retailer);

    Retailer findByPhone(String phone);

    List<Retailer> getAll();

    Map<String, Long> getRetailerCounts();

    // Same TOTAL and NEW buckets, counted only for one salesman's retailers.
    Map<String, Long> getRetailerCounts(String assignedSalesmanId);

    Page<Retailer> getRetailers(RetailerFilter filter, Pageable pageable);

    Retailer getById(String id);

    Retailer getByIdentifier(String identifier);

    List<Retailer> getAssignedRetailers(String assignedSalesmanId);

    Retailer update(Retailer retailer);
}
