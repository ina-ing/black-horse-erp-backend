package com.inaing.blackhorse_erp.module.product.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.product.domain.Product;

public interface IProductService {
    Product create(Product product);

    Product getById(String id);

    Product getByIdentifier(String identifier);

    List<Product> getAll();

    Product update(Product product);

    void delete(Product product);
}
