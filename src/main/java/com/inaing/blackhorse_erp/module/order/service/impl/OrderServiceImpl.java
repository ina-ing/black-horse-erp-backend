package com.inaing.blackhorse_erp.module.order.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.repository.OrderRepository;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order create(Order order) {
        order.setCode(CodeGeneratorUtil.generateCode(CodeType.ORDER));
        order.setOrderDate(Instant.now());
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getByIdentifier(String identifier) {

        if (UUIDUtils.isUUID(identifier)) {
            return orderRepository.findById(identifier).orElse(null);
        }
        return orderRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAllBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        return orderRepository.save(order);
    }

}
