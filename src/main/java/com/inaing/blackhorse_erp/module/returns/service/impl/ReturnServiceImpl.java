package com.inaing.blackhorse_erp.module.returns.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.dto.projections.ReturnStatusCountProjection;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnFilter;
import com.inaing.blackhorse_erp.module.returns.repository.ReturnRepository;
import com.inaing.blackhorse_erp.module.returns.repository.spec.ReturnSpecifications;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl implements IReturnService {

    private final ReturnRepository returnRepository;

    @Override
    @Transactional
    public Return create(Return ret) {
        ret.setCode(CodeGeneratorUtil.generateCode(CodeType.RETURN));
        ret.setReturnDate(Instant.now());

        return returnRepository.save(ret);
    }

    @Override
    @Transactional(readOnly = true)
    public Return getByIdentifier(String identifier) {

        if (UUIDUtils.isUUID(identifier)) {
            return returnRepository.findById(identifier).orElse(null);
        }
        return returnRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Return> getAll() {
        return returnRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Return> getReturns(ReturnFilter filter, Pageable pageable) {
        Specification<Return> spec = Specification.allOf(
                ReturnSpecifications.returnDateBetween(filter.dateRange()),
                ReturnSpecifications.statusIn(filter.statuses()),
                ReturnSpecifications.reasonIn(filter.reasons()),
                ReturnSpecifications.handledBy(filter.handledById()),
                ReturnSpecifications.placedBy(filter.retailerId()),
                ReturnSpecifications.matchesSearch(filter.search()));

        return returnRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnStatusCountProjection> getStatusCounts(String handledById, String retailerId,
            List<ReturnReason> reasons) {
        return returnRepository.findStatusCounts(handledById, retailerId, reasons);
    }

    @Override
    @Transactional
    public Return update(Return ret) {
        return returnRepository.save(ret);
    }

    @Override
    public List<Return> getAllByStatus(ReturnStatus status) {
        return returnRepository.findAllByStatus(status);
    }

}
