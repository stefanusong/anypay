package com.stefanusong.anypay.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class AnypayConfig {
    @Builder.Default
    private boolean isProduction = false;
    private String MidtransServerKey;
    private String MidtransClientKey;
    private String MidtransWebhookEndpoint;
    private String XenditAPIKey;
}
