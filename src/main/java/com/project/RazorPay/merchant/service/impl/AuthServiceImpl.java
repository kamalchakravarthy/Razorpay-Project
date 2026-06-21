package com.project.RazorPay.merchant.service.impl;

import com.project.RazorPay.common.enums.MerchantStatus;
import com.project.RazorPay.common.enums.UserRole;
import com.project.RazorPay.common.exception.DuplicateResourceException;
import com.project.RazorPay.merchant.dto.request.MerchantSignupRequest;
import com.project.RazorPay.merchant.dto.response.MerchantResponse;
import com.project.RazorPay.merchant.entity.AppUser;
import com.project.RazorPay.merchant.entity.Merchant;
import com.project.RazorPay.merchant.repository.AppUserRepository;
import com.project.RazorPay.merchant.repository.MerchantRepository;
import com.project.RazorPay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {

        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT",
                    "Merchant with email already exists: " + request.email());
        }

        Merchant merchant = Merchant.builder()
                .businessName(request.businessName())
                .businessType(request.businessType())
                .name(request.name())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();

        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) // TODO: encrypt using Bcrypt
                .role(UserRole.OWNER)
                .build();

        appUser = appUserRepository.save(appUser);

        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getEmail(),merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus());
    }
}
