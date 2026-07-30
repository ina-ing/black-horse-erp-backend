package com.inaing.blackhorse_erp.module.product.service.impl;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.repository.ProductRepository;
import com.inaing.blackhorse_erp.module.product.service.IProductService;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product create(Product product) {
        if (productRepository.existsByArticleCode(product.getArticleCode())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "Article code already exists " + product.getArticleCode());
        }
        product.setStatus(ProductStatus.ACTIVE);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Product not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return productRepository.findById(identifier).orElse(null);
        }
        return productRepository.findByArticleCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAllBy();
    }

    @Override
    @Transactional
    public Product update(Product product) {
        Optional<Product> existing = productRepository.findByArticleCode(product.getArticleCode());

        if (existing.isPresent() && !existing.get().getId().equals(product.getId())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "Article code already exists " + product.getArticleCode());
        }
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

}
