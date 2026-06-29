package com.project.RazorPay.merchant.mapper;

import com.project.RazorPay.merchant.dto.response.ApiKeyResponse;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;
import com.project.RazorPay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApikeyCreateResponse toCreateResponse(ApiKey apiKey);

    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
