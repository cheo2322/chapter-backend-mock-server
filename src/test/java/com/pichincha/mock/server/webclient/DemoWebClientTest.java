package com.pichincha.mock.server.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.mock.server.dto.Transaction;
import io.swagger.models.HttpMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class DemoWebClientTest {

    private ClientAndServer mockServer;
    private DemoWebClient demoWebClient;
    private static final ObjectMapper serializer = new ObjectMapper();

    private final int PORT = 9000;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
        demoWebClient = new DemoWebClient();
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

    @Test
    void getTransactions() throws JsonProcessingException {
        String id = "1";
        Transaction transaction = Transaction.builder().transactionId(id).build();
        String stringResponse = serializer.writeValueAsString(transaction);

        mockServer.when(
                request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("api/transactions/" + id)
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(stringResponse)
        );

        Optional<Transaction> response = demoWebClient.getTransaction(id);

        assertNotNull(response);
        assertEquals(1, response);
    }
}