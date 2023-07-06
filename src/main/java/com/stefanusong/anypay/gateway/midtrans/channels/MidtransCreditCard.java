package com.stefanusong.anypay.gateway.midtrans.channels;

import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.stefanusong.anypay.dto.requests.CreditCardRequest;
import com.stefanusong.anypay.dto.requests.ItemRequest;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import com.stefanusong.anypay.dto.responses.creditCard.CreditCardItem;
import com.stefanusong.anypay.dto.responses.creditCard.CreditCardResponseData;
import com.stefanusong.anypay.enums.PaymentGateway;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MidtransCreditCard implements IMidtransChannel{
    @Override
    public Map<String, Object> constructParams(PaymentRequest paymentRequest) throws MidtransError {
        Map<String, Object> chargeParams = new HashMap<>();

        // Payment method
        chargeParams.put("payment_type", "credit_card");
        Map<String, String> creditCardDetail = new HashMap<>();
        creditCardDetail.put("token_id", getTokenId(paymentRequest));
        creditCardDetail.put("authentication", "true");
        chargeParams.put("credit_card", creditCardDetail);

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

    private static String getTokenId(PaymentRequest paymentRequest) throws MidtransError {
        CreditCardRequest creditCard = paymentRequest.getCreditCard();
        JSONObject tokenResp = CoreApi.cardToken(creditCard.cardNumber(), creditCard.expMonth(), creditCard.expYear(), creditCard.cvv());
        String tokenId;

        if(tokenResp != null && tokenResp.getString("status_code").equals("200")) {
            tokenId = tokenResp.getString("token_id");
        } else {
            throw new MidtransError("Failed to create token");
        }
        return tokenId;
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

        // Credit card Item
        CreditCardItem creditCardItem = new CreditCardItem(response.getString("bank"),
                response.getString("masked_card"),
                response.getString("card_type"),
                response.getString("redirect_url"));

        // Final Response
        BaseResponseData baseData = new CreditCardResponseData(baseTransactionDetail, creditCardItem);
        return new BaseResponse(
                Integer.valueOf(response.getString("status_code")),
                response.getString("status_message"),
                baseData);
    }
}
