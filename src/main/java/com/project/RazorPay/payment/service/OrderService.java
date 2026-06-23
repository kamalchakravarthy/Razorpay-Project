package com.project.RazorPay.payment.service;

import com.project.RazorPay.payment.dto.request.OrderCreateRequest;
import com.project.RazorPay.payment.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface OrderService {

    OrderResponse create(OrderCreateRequest request, UUID merchantId);
}
