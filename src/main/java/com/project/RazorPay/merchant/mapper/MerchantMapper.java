package com.project.RazorPay.merchant.mapper;

import com.project.RazorPay.merchant.dto.request.MerchantSignupRequest;
import com.project.RazorPay.merchant.dto.response.MerchantResponse;
import com.project.RazorPay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignupRequest(MerchantSignupRequest merchantSignupRequest);

    MerchantResponse toResponse(Merchant merchant);
}
