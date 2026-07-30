package com.inaing.blackhorse_erp.module.retailer.service;

import java.util.List;
import java.util.Map;

import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;

public interface IRetailerService {

    Retailer create(Retailer retailer);

    String generateRetailerCode();

    Retailer findByPhone(String phone);

    List<Retailer> getAll();

    Map<String, Long> getRetailerCounts();

    Retailer getById(String id);

    Retailer getByIdentifier(String identifier);
}
