package com.inaing.blackhorse_erp.module.product.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductVariantUpdateRequestDto(
        String id,
        @NotBlank String color,
        String imageUrl,
        MultipartFile image,
        @NotEmpty List<String> availableSizes) {

}
