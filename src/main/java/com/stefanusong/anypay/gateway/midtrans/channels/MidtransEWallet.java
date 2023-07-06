package com.stefanusong.anypay.gateway.midtrans.channels;

import com.stefanusong.anypay.dto.requests.ItemRequest;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.*;
import com.stefanusong.anypay.dto.responses.ewallet.ActionItem;
import com.stefanusong.anypay.dto.responses.ewallet.EWalletResponseData;
import com.stefanusong.anypay.enums.PaymentGateway;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class MidtransEWallet implements IMidtransChannel {

    @Override
    public Map<String, Object> constructParams(PaymentRequest paymentRequest) {
        Map<String, Object> chargeParams = new HashMap<>();

        // Payment method
        chargeParams.put("payment_type", paymentRequest.getMethod());

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

        // Actions
        ArrayList<ActionItem> baseActions = new ArrayList<>();
        JSONArray actions = response.getJSONArray("actions");
        Integer actionLength = actions.length();
        for (int i = 0 ; i < actionLength; i++) {
            JSONObject action = actions.getJSONObject(i);
            ActionItem actionItem = new ActionItem(action.getString("name"),
                    action.getString("url"), action.getString("method"));
            baseActions.add(actionItem);
        }

        // Final Response
        BaseResponseData baseData = new EWalletResponseData(baseTransactionDetail, baseActions);
        return new BaseResponse(
                Integer.valueOf(response.getString("status_code")),
                response.getString("status_message"),
                baseData);
    }
}
