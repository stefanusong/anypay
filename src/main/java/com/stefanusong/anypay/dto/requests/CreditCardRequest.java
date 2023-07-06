package com.stefanusong.anypay.dto.requests;

public record CreditCardRequest(String cardNumber, String expMonth, String expYear, String cvv) {
}
