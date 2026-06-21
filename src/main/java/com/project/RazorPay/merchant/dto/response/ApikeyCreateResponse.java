package com.project.RazorPay.merchant.dto.response;

import com.project.RazorPay.common.enums.Environment;

import java.util.UUID;

public record ApikeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
