package com.order.system.payment.service.domain.mapper;

import com.order.system.domain.valueobject.CustomerId;
import com.order.system.domain.valueobject.Money;
import com.order.system.domain.valueobject.OrderId;
import com.order.system.payment.service.domain.dto.PaymentRequest;
import com.order.system.stock.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {

    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
