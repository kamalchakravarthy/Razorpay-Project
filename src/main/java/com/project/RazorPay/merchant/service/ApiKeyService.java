package com.project.RazorPay.merchant.service;

import com.project.RazorPay.merchant.dto.request.CreateApikeyRequest;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;

import java.util.UUID;

public interface ApiKeyService {
    ApikeyCreateResponse create(UUID merchantId, CreateApikeyRequest request);
}
