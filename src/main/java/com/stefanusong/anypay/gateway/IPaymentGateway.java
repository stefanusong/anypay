package com.stefanusong.anypay.gateway;

import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;

public interface IPaymentGateway {
    public BaseResponse charge(PaymentRequest paymentRequest);
}
