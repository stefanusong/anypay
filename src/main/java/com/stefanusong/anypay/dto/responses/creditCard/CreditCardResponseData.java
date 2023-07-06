package com.stefanusong.anypay.dto.responses.creditCard;

import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;

public class CreditCardResponseData extends BaseResponseData {
    public CreditCardResponseData(BaseTransactionDetail transactionDetail, CreditCardItem creditCardItem) {
        super(transactionDetail);
        this.creditCardItem = creditCardItem;
    }

    private CreditCardItem creditCardItem;
}
