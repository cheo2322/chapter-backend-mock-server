package com.pichincha.mock.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.mock.server.dto.Transaction;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    private static ClientAndServer mockServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        mockServer = startClientAndServer(8000);
    }

    @AfterClass
    public static void after() {
        mockServer.stop();
    }

    @Test
    public void getTransactionTest() throws JsonProcessingException {
        String id = "1";
        mockServer.when(request()
                        .withMethod("GET")
                        .withHeader("Content-Type", "application/json")
                        .withPath("/api/transactions/" + id)
                )
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(createTransactionObjectToPut()))
                );

        Optional<Transaction> result = transactionService.getTransaction(id);
        assertNotNull(result.get());
        Transaction transaction = result.get();
        Assert.assertEquals(transaction.getTransactionId(), "1");
        Assert.assertEquals(transaction.getAccount(), "0000");
    }

    public Transaction createTransactionObjectToPut() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1");
        transaction.setDescription("TRANSFERENCIA DIRECTA");
        transaction.setLongDescription("TRANSFERENCIA DIRECTA");
        transaction.setAmount(new BigDecimal(100.0));

        Transaction.CodeAndDescription currency = new Transaction.CodeAndDescription();
        currency.setCode("USD");
        currency.setDescription("DOLARES AMERICANOS");
        transaction.setCurrency(currency);

        transaction.setOperationDate("2021-10-01");

        Transaction.CodeAndDescription type = new Transaction.CodeAndDescription();
        type.setCode("D");
        type.setDescription("DEBITO");
        transaction.setType(type);

        Transaction.TransactionChannel transactionChannel = new Transaction.TransactionChannel();
        type = new Transaction.CodeAndDescription();
        type.setCode("O");
        type.setDescription("OFICINA");
        transactionChannel.setType(type);
        transactionChannel.setName("Agencia 1");
        transaction.setChannel(transactionChannel);

        Transaction.Account account = new Transaction.Account();
        account.setAvailableAmount(new BigDecimal(100.0));
        currency = new Transaction.CodeAndDescription();
        currency.setCode("USD");
        currency.setDescription("DOLARES AMERICANOS");
        account.setCurrency(currency);
        transaction.setAccount(account);

        return transaction;
    }
}
