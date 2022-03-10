package com.pichincha.mock.server.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.service.TransactionService;
import io.swagger.models.HttpMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.Assert;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class TransactionServiceTest {

    private ClientAndServer mockServer;
    private TransactionService demoWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final int PORT = 9000;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
        demoWebClient = new TransactionService();
    }

    @AfterEach
    public void stop() {
        mockServer.stop();
    }

    @Test
    void addTransactionTest() throws JsonProcessingException {

        Transaction newTransaction = createTransactionObjectToPut(null);

        mockServer.when(
                request()
                        .withMethod(HttpMethod.POST.name())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withPath("/api/transactions")
                        .withBody(objectMapper.writeValueAsString(newTransaction))
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(createTransactionObjectToPut("1")))
        );

        Transaction response = demoWebClient.addTransaction(newTransaction);

        assertNotNull(response);

        Assert.assertEquals(response.getTransactionId(), "1");
    }

    @Test
    void getTransactionTest() throws JsonProcessingException {

        String id = "1";

        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withPath("/api/transactions/" + id)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(createTransactionObjectToPut(id)))
        );

        Optional<Transaction> response = demoWebClient.getTransaction(id);

        assertNotNull(response.get());

        Transaction transaction = response.get();

        Assert.assertEquals(transaction.getTransactionId(), id);
        Assert.assertEquals(transaction.getCurrency().getCode(), "USD");
    }

    @Test
    void getAllTransactionsTest() throws JsonProcessingException {

        int numberTransactions = 2;

        List<Transaction> transactionList = createTransactionListObjectToPut(numberTransactions);

        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withPath("/api/transactions")
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(transactionList))
        );

        Flux<Transaction> response = demoWebClient.getAllTransactions();

        assertNotNull(response);

        Assert.assertTrue(response.count().block() == numberTransactions);

        Assert.assertEquals(response.blockFirst().getCurrency().getCode(), "USD");
    }

    @Test
    void deleteTransactionTest() {

        String id = "1";

        mockServer.when(
                request()
                        .withMethod(HttpMethod.DELETE.name())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withPath("/api/transactions/" + id)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody(id)
        );

        String response = demoWebClient.deleteTransaction(id);

        assertNotNull(response);

        Assert.assertEquals(response, id);
    }

    public List<Transaction> createTransactionListObjectToPut(int numberTransactions) {

        List<Transaction> list = new ArrayList<>();

        for (int i = 1; i <= numberTransactions; i++) {
            list.add(createTransactionObjectToPut(String.valueOf(i)));
        }

        return list;
    }

    public Transaction createTransactionObjectToPut(String id) {

        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
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