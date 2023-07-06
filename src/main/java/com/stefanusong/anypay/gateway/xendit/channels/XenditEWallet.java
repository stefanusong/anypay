package com.stefanusong.anypay.gateway.xendit.channels;

import com.stefanusong.anypay.dto.requests.ItemRequest;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import com.stefanusong.anypay.dto.responses.ewallet.ActionItem;
import com.stefanusong.anypay.dto.responses.ewallet.EWalletResponseData;
import com.stefanusong.anypay.enums.PaymentGateway;
import com.stefanusong.anypay.utils.MobileNumberFormatter;
import com.xendit.exception.XenditException;
import com.xendit.model.Customer;
import com.xendit.model.EWalletBasketItem;
import com.xendit.model.EWalletCharge;
import java.util.*;

public class XenditEWallet implements IXenditChannel {
    @Override
    public Map<String, Object> constructParams(PaymentRequest paymentRequest) throws XenditException {
        Map<String, Object> params = new HashMap<>();

        // Channel properties
        Map<String, String> channelProperties = new HashMap<>();
        channelProperties.put("success_redirect_url", paymentRequest.getRedirectURL());
        String formattedCustomerPhone = MobileNumberFormatter.formatToE164(paymentRequest.getCustomer().phone(), "ID");
        channelProperties.put("mobile_number", formattedCustomerPhone);
        params.put("channel_properties", channelProperties);

        // Common parameters
        params.put("reference_id", UUID.randomUUID().toString());
        params.put("currency", "IDR");
        params.put("amount", paymentRequest.getTotalPrice());
        params.put("checkout_method", "ONE_TIME_PAYMENT");
        params.put("channel_code", paymentRequest.getMethod().getValue());

        // Create customer
        Map<String, Object> customerParam = new HashMap<>();
        customerParam.put("reference_id", UUID.randomUUID().toString());

        Map<String, Object> individualDetail = new HashMap<>();
        individualDetail.put("given_names", paymentRequest.getCustomer().firstName());
        individualDetail.put("surname", paymentRequest.getCustomer().lastName());
        customerParam.put("individual_detail", individualDetail);

        customerParam.put("email", paymentRequest.getCustomer().email());
        customerParam.put("phone_number", formattedCustomerPhone);
        customerParam.put("type", "INDIVIDUAL");
        Customer customer = Customer.createCustomer(customerParam);
        params.put("customer_id", customer.getId());


        // Create items
        ArrayList<EWalletBasketItem> items = new ArrayList<>();
        for (ItemRequest item : paymentRequest.getItems()) {
            EWalletBasketItem basketItem = EWalletBasketItem.builder()
                    .referenceId(UUID.randomUUID().toString())
                    .name(item.name())
                    .category("product")
                    .currency("IDR")
                    .price(item.price())
                    .quantity(item.qty())
                    .type("PRODUCT")
                    .build();
            items.add(basketItem);
        }
        params.put("basket", items.toArray());

        return params;
    }

    @Override
    public BaseResponse charge(Map<String, Object> paymentRequest) throws XenditException {
        // Charge
        EWalletCharge charge = EWalletCharge.createEWalletCharge(paymentRequest);

        // Construct response
        BaseTransactionDetail transactionDetail = new BaseTransactionDetail(
                charge.getId(),
                charge.getStatus(),
                PaymentGateway.XENDIT.getValue(),
                charge.getChannelCode(),
                charge.getCurrency(),
                Double.valueOf(charge.getChargeAmount()),
                charge.getCreated()
        );

        ArrayList<ActionItem> actionItems = new ArrayList<>();

        if(charge.getActions() != null) {
            actionItems = new ArrayList<>(List.of(
                    new ActionItem("desktop_web_checkout_url", charge.getActions().get("desktop_web_checkout_url"), "GET"),
                    new ActionItem("mobile_web_checkout_url",  charge.getActions().get("mobile_web_checkout_url"), "GET"),
                    new ActionItem("mobile_deeplink_checkout_url", charge.getActions().get("mobile_deeplink_checkout_url"), "GET"),
                    new ActionItem("qr_checkout_string", charge.getActions().get("qr_checkout_string"), "GET")
            ));
        }

        BaseResponseData data = new EWalletResponseData(transactionDetail, actionItems);
        BaseResponse resp = new BaseResponse(201,  "Transaction is created", data);
        return resp;
    }
}
