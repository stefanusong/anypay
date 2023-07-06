package com.stefanusong.anypay.enums;

public enum PaymentMethod {
    GOPAY("gopay"),
    SHOPEEPAY("shopeepay"),
    OVO("ID_OVO"),
    DANA("ID_DANA"),
    LINKAJA("ID_LINKAJA"),
    BCA_VA("bca"),
    BNI_VA("bni"),
    BRI_VA("bri"),
    MANDIRI_VA("MANDIRI"),
    CIMB_VA("CIMB"),
    PERMATA_VA("PERMATA"),
    CREDIT_CARD("credit_card");

    private String value;
    private PaymentMethod(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
