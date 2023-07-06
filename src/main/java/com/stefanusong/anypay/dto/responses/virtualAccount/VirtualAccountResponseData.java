package com.stefanusong.anypay.dto.responses.virtualAccount;

import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;

import java.util.ArrayList;

public class VirtualAccountResponseData extends BaseResponseData {
    public VirtualAccountResponseData(BaseTransactionDetail transactionDetail, VirtualAccountItem virtualAccountItem) {
        super(transactionDetail);
        this.virtualAccountItem = virtualAccountItem;
    }

    private VirtualAccountItem virtualAccountItem;
}
