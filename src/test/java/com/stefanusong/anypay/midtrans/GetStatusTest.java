package com.stefanusong.anypay.midtrans;

import com.stefanusong.anypay.Anypay;
import com.stefanusong.anypay.config.AnypayConfig;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.enums.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetStatusTest {
    private Anypay anypay;

    @BeforeEach
    void setUp() {
        AnypayConfig anypayConfig = AnypayConfig.builder()
                .isProduction(false)
                .MidtransServerKey("SB-Mid-server-Q-dn3l1Xj7QqLafgyuiIvmu0")
                .MidtransWebhookEndpoint(null)
                .XenditAPIKey("xnd_development_VCtSFFg3O5FhsqwR8qMvICgkhkxWZcd9ylFAjUQq8p7vzKciEWWSjVyoBntE1")
                .build();
        anypay = new Anypay(anypayConfig);
    }

    @Test
    void testGetStatusEWallet() {
        BaseResponse response = anypay.getStatus("2796191c-0db7-44be-b939-a14dd21b3421", PaymentGateway.MIDTRANS);

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.data());
        assertNotNull(response.data().getTransactionDetail());
    }

    @Test
    void testGetStatusVirtualAccount() {
        BaseResponse response = anypay.getStatus("d2b27e05-c93a-4bc3-8e1c-0d08117c3796", PaymentGateway.MIDTRANS);

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.data());
        assertNotNull(response.data().getTransactionDetail());
    }

    @Test
    void testGetStatusCreditCard() {
        BaseResponse response = anypay.getStatus("8e20c825-2e51-4793-9b6d-4812d7d2f2e2", PaymentGateway.MIDTRANS);

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.data());
        assertNotNull(response.data().getTransactionDetail());
    }

    @Test
    void testGetStatusNotFound() {
        BaseResponse response = anypay.getStatus("8e20x021-2e51-4793-9bxd-4812d7d2f2e2", PaymentGateway.MIDTRANS);

        assertNotNull(response);
        assertEquals(404, response.statusCode());
        assertNull(response.data());
    }
}
