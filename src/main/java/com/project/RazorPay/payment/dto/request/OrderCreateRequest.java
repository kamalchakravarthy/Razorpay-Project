package com.project.RazorPay.payment.dto.request;

import com.project.RazorPay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record OrderCreateRequest(

        @NotNull(message = "Amount is required.")
        Money amount,

        @Size(max = 100)
        String receipt, //  order id know to merchant

        Map<String, Object> notes, // {user details}

        LocalDateTime expiresAt
) {
}
