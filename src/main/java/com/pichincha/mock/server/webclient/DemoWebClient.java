package com.pichincha.mock.server.webclient;

import com.pichincha.mock.server.dto.Transaction;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Component
public class DemoWebClient {

  private final WebClient webClient;

  protected DemoWebClient() {
    this.webClient = WebClient.builder()
        .baseUrl("https://run.mocky.io")
        .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
        .build();
  }

  private RequestHeadersSpec<?> setupWebclient() {
    return this.webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/v3/76cedf38-7f06-43ad-b618-7293dc9b0926").build());
  }

  public Flux<Transaction> getTransactions() {
    return setupWebclient()
        .retrieve()
        .bodyToFlux(Transaction.class);
  }
}
