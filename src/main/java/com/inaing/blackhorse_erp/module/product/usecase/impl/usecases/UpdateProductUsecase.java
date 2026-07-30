package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductUsecase {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto execute(String id, ProductUpdateRequestDto request) {
        Product product = productService.getById(id);
        if (product == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No product found with id: " + id);
        }

        Category category = categoryService.getByIdentifier(request.category());
        if (category == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No category with name: " + request.category());
        }
   
        return productMapper.toResponse(productService.update(product));
    }
}
