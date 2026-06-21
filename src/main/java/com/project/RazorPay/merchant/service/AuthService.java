package com.project.RazorPay.merchant.service;

import com.project.RazorPay.merchant.dto.request.MerchantSignupRequest;
import com.project.RazorPay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
