package com.project.RazorPay.merchant.controller;

import com.project.RazorPay.merchant.dto.request.CreateApikeyRequest;
import com.project.RazorPay.merchant.dto.response.ApikeyCreateResponse;
import com.project.RazorPay.merchant.service.ApiKeyService;
import com.project.RazorPay.merchant.service.impl.ApiKeyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApikeyCreateResponse> create(@PathVariable UUID merchantId, @RequestBody CreateApikeyRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId, request));

    }
}
