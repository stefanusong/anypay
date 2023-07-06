package com.stefanusong.anypay.gateway.midtrans.channels;

import com.midtrans.httpclient.error.MidtransError;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import org.json.JSONObject;

import java.util.Map;

public interface IMidtransChannel {
    Map<String, Object> constructParams(PaymentRequest paymentRequest) throws MidtransError;
    BaseResponse constructResponse(JSONObject response);
}
