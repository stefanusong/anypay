package com.stefanusong.anypay.gateway.xendit.channels;

import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.xendit.exception.XenditException;
import org.json.JSONObject;

import java.util.Map;

public interface IXenditChannel {
    Map<String, Object> constructParams(PaymentRequest paymentRequest) throws XenditException;
    BaseResponse charge(Map<String, Object> paymentRequest) throws XenditException;
}
