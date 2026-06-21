package com.project.RazorPay.merchant.dto.request;

import com.project.RazorPay.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(

        @NotNull(message = "Name is required")
        @Size(max = 50, message = "Name should not be more than 50 characters long")
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull
        @NotNull(message = "Password is required")
        String password,

        @Size(max = 50, message = "Business name should should not be more than 50 characters")
        String businessName,

        BusinessType businessType
){

}
