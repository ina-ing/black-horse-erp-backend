package com.inaing.blackhorse_erp.module.production.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.production.domain.Production;

public interface IProductionService {

    Production create(Production production);

    Production update(Production production);

    Production getByIdentifier(String identifier);

    List<Production> getAll();

    String generateProductionCode();
}
