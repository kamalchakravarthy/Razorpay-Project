package com.project.RazorPay.merchant.dto.response;

import com.project.RazorPay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        Boolean isEnabled,
        LocalDateTime lastUsedAt

) {
}
