package com.project.RazorPay.payment.mapper;

import com.project.RazorPay.payment.dto.response.OrderResponse;
import com.project.RazorPay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
