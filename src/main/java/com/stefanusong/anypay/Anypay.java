package com.stefanusong.anypay;

import com.midtrans.Midtrans;
import com.stefanusong.anypay.config.AnypayConfig;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.enums.PaymentGateway;
import com.stefanusong.anypay.factories.GatewayFactory;
import com.stefanusong.anypay.gateway.IPaymentGateway;
import com.stefanusong.anypay.gateway.midtrans.MidtransGateway;
import com.xendit.Xendit;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Anypay {
    public Anypay(AnypayConfig config) {
        Midtrans.isProduction = config.isProduction();
        Midtrans.serverKey = config.getMidtransServerKey();
        Midtrans.clientKey = config.getMidtransClientKey();
        Midtrans.paymentAppendNotification(config.getMidtransWebhookEndpoint());
        Xendit.apiKey = config.getXenditAPIKey();
    }

    public BaseResponse pay(PaymentRequest paymentRequest) {
        log.info("[Anypay] payment request: {}", paymentRequest);
        IPaymentGateway gateway = GatewayFactory.GetGatewayByMethod(paymentRequest.getMethod());
        BaseResponse response = gateway.charge(paymentRequest);
        log.info("[Anypay] payment response: {}", response);
        return response;
    }

    public BaseResponse getStatus(String transactionId, PaymentGateway gateway) {
        log.info("[Anypay] get status request: {} {}", transactionId, gateway);
        if(gateway.equals(PaymentGateway.MIDTRANS)) {
            MidtransGateway midtransGateway = new MidtransGateway(null);
            BaseResponse response = midtransGateway.getStatus(transactionId);
            return response;
        } else if(gateway.equals(PaymentGateway.XENDIT)) {
            return new BaseResponse(501, "Get status for xendit is not yet implemented", null);
        }
        return new BaseResponse(400, "Invalid payment gateway", null);
    }

}
