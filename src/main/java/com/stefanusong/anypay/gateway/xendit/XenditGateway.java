package com.stefanusong.anypay.gateway.xendit;

import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.gateway.IPaymentGateway;
import com.stefanusong.anypay.gateway.xendit.channels.IXenditChannel;
import com.xendit.exception.XenditException;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
@Log4j2
public class XenditGateway implements IPaymentGateway {
    private final IXenditChannel xenditChannel;

    public XenditGateway(IXenditChannel xenditChannel) {
        this.xenditChannel = xenditChannel;
    }

    @Override
    public BaseResponse charge(PaymentRequest paymentRequest) {
        try {
            Map<String, Object> chargeParams = xenditChannel.constructParams(paymentRequest);
            BaseResponse response = xenditChannel.charge(chargeParams);
            return response;
        } catch(XenditException e) {
            log.error("Xendit exception: {}", e.getMessage());
            return new BaseResponse(500, e.getMessage(), null);
        } catch(Exception e) {
            log.error("Exception: {}", e.getMessage());
            return new BaseResponse(500, e.getMessage(), null);
        }
    }
}
