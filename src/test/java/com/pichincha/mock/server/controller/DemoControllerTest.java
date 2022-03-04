package com.pichincha.mock.server.controller;

import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.webclient.DemoWebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.Expectation;
import org.mockserver.model.MediaType;
import org.mockserver.model.RequestDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Cookie.cookie;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

class DemoControllerTest {

    private static ClientAndServer mockServer;

    @Autowired
    DemoWebClient demoWebClient;

    @BeforeAll
    public static void startMockServer() {
        mockServer = startClientAndServer(8081);

        Expectation[] expectations = new MockServerClient("localhost", 8081)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/default/transactions")
                                .withCookies(
                                        cookie("session", "4930456C-C718-476F-971F-CB8E047AB349")
                                )
                                .withQueryStringParameters(
                                        param("cartId", "055CA455-1DF7-45BB-8535-4F83E7266092")
                                )
                )
                .respond(
                        response()
                                .withBody("some_response_body")
                );
        System.out.println(Arrays.toString(expectations));

        RequestDefinition[] requestDefinitions = new MockServerClient("localhost", 8081)
                .retrieveRecordedRequests(
                        request()
                                .withPath("/view/cart")
                                .withQueryStringParameter("cartId", "055CA455-1DF7-45BB-8535-4F83E7266092")
                                .withMethod("GET")
                );
        System.out.println(Arrays.toString(requestDefinitions));
    }

    @AfterAll
    public static void stopMockServer() {
        mockServer.stop();
    }

    @Test
    void getTransactions() {

    }

    @Test
    void unitTestCaseGetTransactionById() {
        // Send instruction to MockServer for the mock response setup via MockServerClient
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/default/transactions/1")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                                .withBody(
                                        "{" +
                                                "\"transactionId\": \"1\", \"description\": \"TRANSFERENCIA DIRECTA\", " +
                                                "\"longDescription\": \"TRANSFERENCIA DIRECTA\", " +
                                                "\"amount\": 100.0, " +
                                                "\"currency\": { \"code\": \"USD\", \"description\": \"DOLARES AMERICANOS\" }, " +
                                                "\"operationDate\": \"2021-10-01\", " +
                                                "\"type\": { \"code\": \"D\", \"description\": \"DEBITO\" }, " +
                                                "\"channel\": { \"type\": { \"code\": \"O\", \"description\": \"OFICINA\" }, \"name\": \"Agencia 1\" }, " +
                                                "\"account\": { \"availableAmount\": 100.0, \"currency\": { \"code\": \"USD\", \"description\": \"DOLARES AMERICANOS\" } } " +
                                                "}"
                                )
                );

        String transactionId = "1d42986f-a6d0-40c2-a807-bd4b2ec8472f";

        Optional<Transaction> productOptional = demoWebClient.getTransactionById(transactionId);

        // Assert response
        assertTrue(productOptional.isPresent());
        Transaction transaction = productOptional.get();
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getAccount());
        assertTrue(transaction.getAmount().intValue() > 0);
    }
}