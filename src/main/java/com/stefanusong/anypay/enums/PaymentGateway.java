package com.stefanusong.anypay.enums;

public enum PaymentGateway {
    MIDTRANS("Midtrans"),
    XENDIT("Xendit");

    private String value;
    private PaymentGateway(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
