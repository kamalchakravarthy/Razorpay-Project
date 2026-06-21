package com.project.RazorPay.merchant.service.impl;

import com.project.RazorPay.common.exception.ResourceNotFoundException;
import com.project.RazorPay.common.util.RandomizerUtil;
import com.project.RazorPay.merchant.dto.request.CreateApikeyRequest;
import com.project.RazorPay.merchant.dto.response.ApiKeyResponse;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;
import com.project.RazorPay.merchant.entity.ApiKey;
import com.project.RazorPay.merchant.entity.Merchant;
import com.project.RazorPay.merchant.repository.ApiKeyRepository;
import com.project.RazorPay.merchant.repository.MerchantRepository;
import com.project.RazorPay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public ApikeyCreateResponse create(UUID merchantId, CreateApikeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toUpperCase()+ "_" + RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with BCryptEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApikeyCreateResponse(apiKey.getId(),keyId,rawSecret,apiKey.getEnvironment());

    }

    @Override
    public List<ApiKeyResponse> listByMerchantId(UUID merchantId) {

        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(apiKey ->
                        new ApiKeyResponse(
                                apiKey.getId(),
                                apiKey.getKeyId(),
                                apiKey.getEnvironment(),
                                apiKey.isEnabled(),
                                apiKey.getLastUsedAt()))
                .toList();
    }

    @Override
    @Transactional
    public void revokeApiKey(UUID merchantId, UUID apiKeyId) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        ApiKey key = apiKeyRepository.findById(apiKeyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", apiKeyId));

        key.setEnabled(false);

    }

    @Override
    @Transactional
    public ApikeyCreateResponse rotate(UUID merchantId, UUID apiKeyId) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", apiKeyId));

        String newRawSecret = RandomizerUtil.randomBase64(40);
        String prevSecret = apiKey.getKeySecretHash();  // TODO: encode with BCryptEncoder
        apiKey.setPreviousKeySecretHash(prevSecret);
        apiKey.setKeySecretHash(newRawSecret);
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

        apiKey = apiKeyRepository.save(apiKey);

        return new ApikeyCreateResponse(apiKeyId, apiKey.getKeyId(),apiKey.getKeySecretHash(),apiKey.getEnvironment());
    }
}
