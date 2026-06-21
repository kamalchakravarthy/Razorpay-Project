package com.project.RazorPay.merchant.service.impl;

import com.project.RazorPay.common.exception.ResourceNotFoundException;
import com.project.RazorPay.merchant.dto.request.CreateApikeyRequest;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;
import com.project.RazorPay.merchant.entity.ApiKey;
import com.project.RazorPay.merchant.entity.Merchant;
import com.project.RazorPay.merchant.repository.ApiKeyRepository;
import com.project.RazorPay.merchant.repository.MerchantRepository;
import com.project.RazorPay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public ApikeyCreateResponse create(UUID merchantId, CreateApikeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toUpperCase()+"_big_random_string";
        String rawSecret = "big_random_string"; // TODO: replace with cryptographic random hex

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with BCryptEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApikeyCreateResponse(apiKey.getId(),keyId,rawSecret,apiKey.getEnvironment());

    }
}
