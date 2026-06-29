package com.project.RazorPay.payment.service;

import com.project.RazorPay.payment.dto.request.OrderCreateRequest;
import com.project.RazorPay.payment.dto.response.OrderResponse;
import com.project.RazorPay.payment.dto.response.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface OrderService {

    OrderResponse create(OrderCreateRequest request, UUID merchantId);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancelOrder(UUID merchatId, UUID orderId);

    List<PaymentResponse> listAllPayments(UUID merchantId, UUID orderId);
}
