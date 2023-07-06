package com.stefanusong.anypay.gateway.xendit.channels;

import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import com.stefanusong.anypay.dto.responses.virtualAccount.VirtualAccountItem;
import com.stefanusong.anypay.dto.responses.virtualAccount.VirtualAccountResponseData;
import com.stefanusong.anypay.enums.PaymentGateway;
import com.xendit.exception.XenditException;
import com.xendit.model.FixedVirtualAccount;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XenditVirtualAccount implements IXenditChannel {
    @Override
    public Map<String, Object> constructParams(PaymentRequest paymentRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("external_id", UUID.randomUUID().toString());
        params.put("bank_code", paymentRequest.getMethod().getValue());
        params.put("expected_amount", paymentRequest.getTotalPrice());
        params.put("name", paymentRequest.getCustomer().firstName() + " " + paymentRequest.getCustomer().firstName());

        return params;
    }

    @Override
    public BaseResponse charge(Map<String, Object> paymentRequest) throws XenditException {
        // Charge
        FixedVirtualAccount charge = FixedVirtualAccount.createClosed(paymentRequest);

        // Construct response
        BaseTransactionDetail transactionDetail = new BaseTransactionDetail(
                charge.getId(),
                charge.getStatus(),
                PaymentGateway.XENDIT.getValue(),
                charge.getBankCode(),
                charge.getCurrency(),
                Double.valueOf(charge.getExpectedAmount()),
                LocalDateTime.now().toString()
        );

        // VA Item
        VirtualAccountItem vaItem = new VirtualAccountItem(charge.getBankCode(), charge.getAccountNumber());

        BaseResponseData data = new VirtualAccountResponseData(transactionDetail, vaItem);
        BaseResponse resp = new BaseResponse(201,  "Transaction is created", data);
        return resp;
    }
}
