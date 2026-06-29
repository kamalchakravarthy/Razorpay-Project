package com.project.RazorPay.payment.dto.response;

import com.project.RazorPay.common.entity.Money;
import com.project.RazorPay.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(

        UUID id,
        UUID merchantId,
        String receipt,
        OrderStatus status,
        Integer attempts,
        Map<String, Object> notes,
        Money amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
){

}
