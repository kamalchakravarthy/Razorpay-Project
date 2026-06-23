package com.project.RazorPay.payment.controller;

import com.project.RazorPay.payment.dto.request.OrderCreateRequest;
import com.project.RazorPay.payment.dto.response.OrderResponse;
import com.project.RazorPay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    UUID merchantId = UUID.fromString("30cc4ec7-5b4f-4bf0-884d-61efa55101ca");
    // TODO: Replace with Merchant Context

    @PostMapping()
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(request, merchantId));
    }
}
