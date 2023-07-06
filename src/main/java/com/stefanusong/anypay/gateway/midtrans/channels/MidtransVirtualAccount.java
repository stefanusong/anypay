package com.stefanusong.anypay.gateway.midtrans.channels;

import com.stefanusong.anypay.dto.requests.ItemRequest;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import com.stefanusong.anypay.dto.responses.virtualAccount.VirtualAccountItem;
import com.stefanusong.anypay.dto.responses.virtualAccount.VirtualAccountResponseData;
import com.stefanusong.anypay.enums.PaymentGateway;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MidtransVirtualAccount implements IMidtransChannel {
    @Override
    public Map<String, Object> constructParams(PaymentRequest paymentRequest) {
        Map<String, Object> chargeParams = new HashMap<>();

        // Payment method
        chargeParams.put("payment_type", "bank_transfer");
        Map<String, Object> bankDetail = new HashMap<>();
        bankDetail.put("bank", paymentRequest.getMethod().getValue());
        chargeParams.put("bank_transfer", bankDetail);

        // Callback
        Map<String, Object> callback = new HashMap<>();
        callback.put("enable_callback", paymentRequest.getRedirectURL() != null);
        callback.put("callback_url", paymentRequest.getRedirectURL());
        chargeParams.put( paymentRequest.getMethod().getValue(), callback);

        // Transaction Detail
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", UUID.randomUUID().toString());
        transactionDetails.put("gross_amount", paymentRequest.getTotalPrice());
        chargeParams.put("transaction_details", transactionDetails);

        // Item Detail
        ArrayList<Map> itemDetails = new ArrayList<>();
        for (ItemRequest item : paymentRequest.getItems()) {
            Map<String, Object> itemDetail = new HashMap<>();
            itemDetail.put("name", item.name());
            itemDetail.put("quantity", item.qty());
            itemDetail.put("price", item.price());
            itemDetails.add(itemDetail);
        }
        chargeParams.put("item_details", itemDetails);

        // Customer Detail
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", paymentRequest.getCustomer().firstName());
        customerDetails.put("last_name", paymentRequest.getCustomer().lastName());
        customerDetails.put("email", paymentRequest.getCustomer().email());
        customerDetails.put("phone", paymentRequest.getCustomer().phone());
        chargeParams.put("customer_details", customerDetails);

        return chargeParams;
    }

    @Override
    public BaseResponse constructResponse(JSONObject response) {
        // Transaction Detail
        BaseTransactionDetail baseTransactionDetail = new BaseTransactionDetail(
                response.getString("transaction_id"),
                response.getString("transaction_status"),
                PaymentGateway.MIDTRANS.getValue(),
                response.getString("payment_type"),
                response.getString("currency"),
                Double.valueOf(response.getString("gross_amount")),
                response.getString("transaction_time"));

        // VA Item
        JSONArray vaNumbers = response.getJSONArray("va_numbers");
        JSONObject vaNumber = vaNumbers.getJSONObject(0);
        VirtualAccountItem vaItem = new VirtualAccountItem(vaNumber.getString("bank"),
                vaNumber.getString("va_number"));

        // Final Response
        BaseResponseData baseData = new VirtualAccountResponseData(baseTransactionDetail, vaItem);
        return new BaseResponse(
                Integer.valueOf(response.getString("status_code")),
                response.getString("status_message"),
                baseData);
    }
}
