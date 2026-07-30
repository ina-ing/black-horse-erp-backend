package com.inaing.blackhorse_erp.module.retailer.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.repository.RetailerRepository;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements IRetailerService {

    private final RetailerRepository retailerRepository;

    @Override
    @Transactional
    public Retailer create(Retailer retailer) {
        if (retailerRepository.existsByPhone(retailer.getPhone())) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE,
                    "Phone number already registered " + retailer.getPhone());
        }
        retailer.setCode(generateRetailerCode());
        retailer.setJoinedOn(LocalDate.now());
        return retailerRepository.save(retailer);
    }

    @Override
    @Transactional
    public String generateRetailerCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = CodeGeneratorUtil.generate("RET-", 6);

            if (!retailerRepository.existsByCode(code)) {
                 return code;
            }
        }

        throw new AppException(ErrorCode.IDENTIFIER_ALREADY_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Retailer findByPhone(String phone) {
        return retailerRepository.findByPhone(phone).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getAll() {
        return retailerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRetailerCounts() {
        Map<String, Long> countMap = new HashMap<>();

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        countMap.put("TOTAL", retailerRepository.count());
        countMap.put("NEW", retailerRepository.countByJoinedOnBetween(startOfMonth, endOfMonth));

        return countMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Retailer getById(String id) {
        return retailerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Retailer getByIdentifier(String identifier) {

        if (UUIDUtils.isUUID(identifier)) {
            return retailerRepository.findById(identifier).orElse(null);
        }
        if (identifier.matches("^\\+?\\d{10,15}$")) {
            return retailerRepository.findByPhone(identifier).orElse(null);
        }
        return retailerRepository.findByCode(identifier).orElse(null);
    }

}
