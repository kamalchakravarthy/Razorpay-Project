package com.project.RazorPay.merchant.service;

import com.project.RazorPay.merchant.dto.request.CreateApikeyRequest;
import com.project.RazorPay.merchant.dto.response.ApiKeyResponse;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApikeyCreateResponse create(UUID merchantId, CreateApikeyRequest request);

    List<ApiKeyResponse> listByMerchantId(UUID merchantId);

    void revokeApiKey(UUID merchantId, UUID apiKeyId);

    ApikeyCreateResponse rotate(UUID merchantId, UUID apiKeyId);
}
