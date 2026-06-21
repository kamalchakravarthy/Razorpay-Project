package com.project.RazorPay.merchant.dto.response;

import com.project.RazorPay.common.enums.BusinessType;
import com.project.RazorPay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
