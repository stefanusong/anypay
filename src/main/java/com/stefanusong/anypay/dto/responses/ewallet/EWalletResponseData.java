package com.stefanusong.anypay.dto.responses.ewallet;

import com.stefanusong.anypay.dto.responses.BaseResponseData;
import com.stefanusong.anypay.dto.responses.BaseTransactionDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class EWalletResponseData extends BaseResponseData {
    public EWalletResponseData(BaseTransactionDetail transactionDetail, ArrayList<ActionItem> actions) {
        super(transactionDetail);
        this.actions = actions;
    }

    private ArrayList<ActionItem> actions;


}
