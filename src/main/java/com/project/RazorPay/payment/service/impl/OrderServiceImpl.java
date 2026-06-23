package com.project.RazorPay.payment.service.impl;

import com.project.RazorPay.common.exception.DuplicateResourceException;
import com.project.RazorPay.payment.dto.request.OrderCreateRequest;
import com.project.RazorPay.payment.dto.response.OrderResponse;
import com.project.RazorPay.payment.entity.OrderRecord;
import com.project.RazorPay.payment.repository.OrderRepository;
import com.project.RazorPay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryInMinutes;

    @Override
    public OrderResponse create(OrderCreateRequest request, UUID merchantId) {

        if(request.receipt() != null
                && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE",
                    "Order with Receipt already exists "+ request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                .amount(request.amount())
                .notes(request.notes())
                .receipt(request.receipt())
                .merchantId(merchantId)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryInMinutes))
                .build();
// TODO: public kafka event about order creation
        order = orderRepository.save(order);

        return new OrderResponse(order.getId(), order.getMerchantId(), order.getReceipt(),
                order.getOrderStatus(), order.getAttempts(), order.getNotes(), order.getAmount(),
                LocalDateTime.now(), order.getExpiresAt());
        // CreatedAt is handled by Auditing
    }
}
