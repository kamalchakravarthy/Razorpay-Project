package com.project.RazorPay.payment.service.impl;

import com.project.RazorPay.common.enums.OrderStatus;
import com.project.RazorPay.common.exception.BusinessRuleViolationException;
import com.project.RazorPay.common.exception.DuplicateResourceException;
import com.project.RazorPay.common.exception.ResourceNotFoundException;
import com.project.RazorPay.merchant.entity.Merchant;
import com.project.RazorPay.merchant.repository.MerchantRepository;
import com.project.RazorPay.payment.dto.request.OrderCreateRequest;
import com.project.RazorPay.payment.dto.response.OrderResponse;
import com.project.RazorPay.payment.dto.response.PaymentResponse;
import com.project.RazorPay.payment.entity.OrderRecord;
import com.project.RazorPay.payment.entity.Payment;
import com.project.RazorPay.payment.mapper.OrderMapper;
import com.project.RazorPay.payment.mapper.PaymentMapper;
import com.project.RazorPay.payment.repository.OrderRepository;
import com.project.RazorPay.payment.repository.PaymentRepository;
import com.project.RazorPay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryInMinutes;

    @Override
    @Transactional
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

        return orderMapper.toResponse(order);
        // CreatedAt is handled by Auditing
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {

        merchantRepository.findById(merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Merchant", merchantId)
        );

        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Order", orderId)
        );

        return orderMapper.toResponse(orderRecord);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId) {
        merchantRepository.findById(merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Merchant", merchantId)
        );

        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Order", orderId)
        );

        if(orderRecord.getOrderStatus() == OrderStatus.CANCELLED || orderRecord.getOrderStatus() == OrderStatus.PAID){
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Cannot cancel order with status: " + orderRecord.getOrderStatus().name());
        }

        orderRecord.setOrderStatus(OrderStatus.CANCELLED);
        orderRecord = orderRepository.save(orderRecord);

        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public List<PaymentResponse> listAllPayments(UUID merchantId, UUID orderId) {

        merchantRepository.findById(merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Merchant", merchantId)
        );

        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Order", orderId)
        );

        List<Payment> payments = paymentRepository.findByOrder_Id(orderId);

        return paymentMapper.toResponseList(payments);
    }
}
