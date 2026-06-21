package com.project.RazorPay.merchant.dto.request;

import com.project.RazorPay.common.enums.Environment;

public record CreateApikeyRequest(
        Environment environment
) {
}
