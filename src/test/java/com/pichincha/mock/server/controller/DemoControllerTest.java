package com.pichincha.mock.server.controller;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Cookie.cookie;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.Expectation;
import org.mockserver.model.RequestDefinition;

class DemoControllerTest {

  private static ClientAndServer mockServer;

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
}