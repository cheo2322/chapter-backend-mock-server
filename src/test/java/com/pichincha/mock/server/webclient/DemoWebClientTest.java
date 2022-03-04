package com.pichincha.mock.server.webclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.properties.DemoProperties;
import io.swagger.models.HttpMethod;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;

class DemoWebClientTest {

  private ClientAndServer mockServer;
  private DemoWebClient demoWebClient;
  private static final ObjectMapper serializer = new ObjectMapper();

  @BeforeEach
  public void setupMockServer() {
    mockServer = ClientAndServer.startClientAndServer(2001);

    DemoProperties demoProperties = new DemoProperties();
    demoProperties.setBaseUrl("http://localhost:" + mockServer.getLocalPort());
    demoWebClient = new DemoWebClient(demoProperties);
  }

  @AfterEach
  public void tearDownServer() {
    mockServer.stop();
  }

  @Test
  void getTransactions() throws JsonProcessingException {
    Transaction transaction = Transaction.builder().transactionId("1").build();
    String stringResponse = serializer.writeValueAsString(transaction);

    mockServer.when(
        request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/v3/76cedf38-7f06-43ad-b618-7293dc9b0926")
    ).respond(
        response()
            .withStatusCode(HttpStatus.OK.value())
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(stringResponse)
    );

    List<Transaction> responses = demoWebClient.getTransactions().collectList().block();

    assertNotNull(responses);
    assertEquals(1, responses.size());
  }
}