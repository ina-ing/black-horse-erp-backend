package com.inaing.blackhorse_erp.module.factory.service;

import com.inaing.blackhorse_erp.module.factory.domain.Factory;

public interface IFactoryService {

    Factory create(Factory factory);

    Factory update(Factory factory);

    Factory getByIdentifier(String identifier);

    Factory getByManagerId(String managerId);

    String generateFactoryCode();
}
