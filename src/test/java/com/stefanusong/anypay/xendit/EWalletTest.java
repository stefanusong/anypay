package com.stefanusong.anypay.xendit;

import com.stefanusong.anypay.Anypay;
import com.stefanusong.anypay.config.AnypayConfig;
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

class EWalletTest {
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
    void testOVO() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest("Stefanus", "Ong",
                "085163551303", "stefanusoms@gmail.com");
        ArrayList<ItemRequest> itemRequests = new ArrayList<>(List.of(
                new ItemRequest("Vanilla Ice Cream", 1, 5000.00),
                new ItemRequest("Chocolate Ice Cream", 2, 10000.00)
        ));
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .method(PaymentMethod.OVO)
                .redirectURL("https://google.com")
                .items(itemRequests)
                .customer(customerRequest)
                .build();

        // Act
        BaseResponse response = anypay.pay(paymentRequest);

        // Assert
        assertEquals(201, response.statusCode());
        assertNotNull(response);
        assertNotNull(response.data());
    }

    @Test
    void testDana() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest("Stefanus", "Ong",
                "085163551303", "stefanusoms@gmail.com");
        ArrayList<ItemRequest> itemRequests = new ArrayList<>(List.of(
                new ItemRequest("Vanilla Ice Cream", 1, 5000.00),
                new ItemRequest("Chocolate Ice Cream", 2, 10000.00)
        ));
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .method(PaymentMethod.DANA)
                .redirectURL("https://google.com")
                .items(itemRequests)
                .customer(customerRequest)
                .build();

        // Act
        BaseResponse response = anypay.pay(paymentRequest);

        // Assert
        assertEquals(201, response.statusCode());
        assertNotNull(response);
        assertNotNull(response.data());
    }

    @Test
    void testLinkAja() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest("Stefanus", "Ong",
                "085163551303", "stefanusoms@gmail.com");
        ArrayList<ItemRequest> itemRequests = new ArrayList<>(List.of(
                new ItemRequest("Vanilla Ice Cream", 1, 12000.00),
                new ItemRequest("Chocolate Ice Cream", 2, 5000.00)
        ));
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .method(PaymentMethod.LINKAJA)
                .redirectURL("https://google.com")
                .items(itemRequests)
                .customer(customerRequest)
                .build();

        // Act
        BaseResponse response = anypay.pay(paymentRequest);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.statusCode());
        assertNotNull(response.data());
    }

}
