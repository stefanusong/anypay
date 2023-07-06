package com.stefanusong.anypay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponseData {
    private BaseTransactionDetail transactionDetail;
}
