package com.stefanusong.anypay.gateway.midtrans;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import com.stefanusong.anypay.enums.PaymentGateway;
import com.stefanusong.anypay.gateway.IPaymentGateway;
import com.stefanusong.anypay.gateway.midtrans.channels.IMidtransChannel;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.util.Map;

@Log4j2
public class MidtransGateway implements IPaymentGateway {
    private final IMidtransChannel midtransChannel;
    private final MidtransCoreApi coreApi;

    public MidtransGateway(IMidtransChannel midtransChannel) {
        this.midtransChannel = midtransChannel;
        Config configOptions = Config.builder()
                .enableLog(true)
                .setIsProduction(Midtrans.isProduction)
                .setServerKey(Midtrans.serverKey)
                .setClientKey(Midtrans.clientKey)
                .build();
        coreApi = new ConfigFactory(configOptions).getCoreApi();
    }

    @Override
    public BaseResponse charge(PaymentRequest paymentRequest) {
        try {
            Map<String, Object> chargeParams = midtransChannel.constructParams(paymentRequest);
            JSONObject response = coreApi.chargeTransaction(chargeParams);
            return midtransChannel.constructResponse(response);
        } catch(MidtransError e) {
            log.error("Midtrans exception: {}", e.getMessage());
            return new BaseResponse(e.getStatusCode(), e.getResponseBody(), null);
        } catch(Exception e) {
            log.error("Exception: {}", e.getMessage());
            return new BaseResponse(500, e.getMessage(), null);
        }
    }

    public BaseResponse getStatus(String transactionId) {
        try {
            JSONObject transactionResp = coreApi.checkTransaction(transactionId);
            BaseTransactionDetail transactionDetail = new BaseTransactionDetail(
                    transactionResp.getString("transaction_id"),
                    transactionResp.getString("transaction_status"),
                    PaymentGateway.MIDTRANS.getValue(),
                    transactionResp.getString("payment_type"),
                    transactionResp.getString("currency"),
                    Double.valueOf(transactionResp.getString("gross_amount")),
                    transactionResp.getString("transaction_time"));
            BaseResponseData data = new BaseResponseData(transactionDetail);

            return new BaseResponse(200, transactionResp.getString("status_message"), data);
        } catch (MidtransError e) {
            log.error("Midtrans exception: {}", e.getMessage());
            return new BaseResponse(e.getStatusCode(), e.getResponseBody(), null);
        } catch(Exception e) {
            log.error("Exception: {}", e.getMessage());
            return new BaseResponse(500, e.getMessage(), null);
        }
    }

}
