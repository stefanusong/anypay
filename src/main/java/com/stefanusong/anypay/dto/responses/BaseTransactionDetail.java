package com.stefanusong.anypay.dto.responses;

public record BaseTransactionDetail(
        String transactionId,
        String status,
        String gateway,
        String paymentMethod,
        String currency,
        Double amount,
        String transactionTime) {
}
