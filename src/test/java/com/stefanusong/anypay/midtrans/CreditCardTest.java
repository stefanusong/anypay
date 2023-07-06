package com.stefanusong.anypay.midtrans;

import com.stefanusong.anypay.Anypay;
import com.stefanusong.anypay.config.AnypayConfig;
import com.stefanusong.anypay.dto.requests.CreditCardRequest;
import com.stefanusong.anypay.dto.requests.CustomerRequest;
import com.stefanusong.anypay.dto.requests.ItemRequest;
import com.stefanusong.anypay.dto.requests.PaymentRequest;
import com.stefanusong.anypay.dto.responses.BaseResponse;
import com.stefanusong.anypay.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreditCardTest {
    private Anypay anypay;

    @BeforeEach
    void setUp() {
        AnypayConfig anypayConfig = AnypayConfig.builder()
                .isProduction(false)
                .MidtransServerKey("SB-Mid-server-Q-dn3l1Xj7QqLafgyuiIvmu0")
                .MidtransClientKey("SB-Mid-client-VTdYPyKaeId7KkdU")
                .MidtransWebhookEndpoint(null)
                .XenditAPIKey("xnd_development_VCtSFFg3O5FhsqwR8qMvICgkhkxWZcd9ylFAjUQq8p7vzKciEWWSjVyoBntE1")
                .build();
        anypay = new Anypay(anypayConfig);
    }

    @Test
    void testCreditCard() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest("Stefanus", "Ong",
                "08123456789", "stefanusoms@gmail.com");
        ArrayList<ItemRequest> itemRequests = new ArrayList<>(List.of(
                new ItemRequest("Matcha Ice Cream", 1, 9000.00)
        ));
        CreditCardRequest creditCardRequest = new CreditCardRequest(
                "4811111111111114",
                "02",
                "2024",
                "123"
        );
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .method(PaymentMethod.CREDIT_CARD)
                .redirectURL(null)
                .items(itemRequests)
                .customer(customerRequest)
                .creditCard(creditCardRequest)
                .build();

        // Act
        BaseResponse response = anypay.pay(paymentRequest);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.statusCode());
        assertNotNull(response.data());
    }
}
