package com.inaing.blackhorse_erp.module.returns.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.repository.ReturnRepository;
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
        ret.setCode(this.generateReturnCode());
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
    @Transactional
    public Return update(Return ret) {
        return returnRepository.save(ret);
    }

    @Override
    public String generateReturnCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = CodeGeneratorUtil.generate("RE-", 7);

            if (!returnRepository.existsByCode(code)) {
                return code;
            }
        }
        throw new AppException(ErrorCode.IDENTIFIER_ALREADY_EXISTS);
    }
}
