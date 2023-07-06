package com.stefanusong.anypay.dto.requests;

import com.stefanusong.anypay.enums.PaymentMethod;

import java.util.ArrayList;

import lombok.*;

@Builder
@Getter
@Setter
public class PaymentRequest {
    @NonNull
    private PaymentMethod method;
    private String redirectURL;
    @NonNull
    private ArrayList<ItemRequest> items;
    @NonNull
    private CustomerRequest customer;
    private CreditCardRequest creditCard;

    public Double getTotalPrice() {
        Double totalPrice = Double.valueOf(0);
        for (ItemRequest item : items) {
            totalPrice += item.price() * item.qty();
        }

        return totalPrice;
    }
}
