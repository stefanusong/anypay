package com.stefanusong.anypay.dto.responses.creditCard;

public record CreditCardItem(String bank, String maskedCard, String cardType, String redirectURL) {
}
