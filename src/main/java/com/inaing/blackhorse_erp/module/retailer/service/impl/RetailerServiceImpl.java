package com.inaing.blackhorse_erp.module.retailer.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerFilter;
import com.inaing.blackhorse_erp.module.retailer.repository.RetailerRepository;
import com.inaing.blackhorse_erp.module.retailer.repository.spec.RetailerSpecifications;
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
        retailer.setCode(CodeGeneratorUtil.generateCode(CodeType.RETAILER));
        retailer.setJoinedOn(LocalDate.now());
        return retailerRepository.save(retailer);
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
        countMap.put("PROVINCES", retailerRepository.countDistinctProvinces(null));

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

    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getAssignedRetailers(String assignedSalesmanId) {
        return retailerRepository.findByAssignedSalesmanId(assignedSalesmanId);
    }

    @Override
    @Transactional
    public Retailer update(Retailer retailer) {
        return retailerRepository.save(retailer);
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRetailerCounts(String assignedSalesmanId) {
        if (assignedSalesmanId == null) {
            return getRetailerCounts();
        }

        Map<String, Long> countMap = new HashMap<>();

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        countMap.put("TOTAL", retailerRepository.countByAssignedSalesmanId(assignedSalesmanId));
        countMap.put("NEW", retailerRepository.countByAssignedSalesmanIdAndJoinedOnBetween(
                assignedSalesmanId, startOfMonth, endOfMonth));
        countMap.put("PROVINCES", retailerRepository.countDistinctProvinces(assignedSalesmanId));

        return countMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Retailer> getRetailers(RetailerFilter filter, Pageable pageable) {
        Specification<Retailer> spec = Specification.allOf(
                RetailerSpecifications.provinceIs(filter.province()),
                RetailerSpecifications.businessTypeIs(filter.businessType()),
                RetailerSpecifications.assignedTo(filter.assignedSalesmanId()),
                RetailerSpecifications.matchesSearch(filter.search()));

        return retailerRepository.findAll(spec, pageable);
    }
}
