package com.pichincha.mock.server.webclient;

import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.properties.DemoProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

@Component
public class DemoWebClient {

    private final WebClient webClient;

    private final DemoProperties demoProperties;

    public DemoWebClient(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;

        this.webClient = WebClient.builder()
                .baseUrl(this.demoProperties.getBaseUrl())
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

    public Optional<Transaction> getTransactionById(String id) {
        Mono<Transaction> products = webClient.get()
                .uri("v3/" + id)//1d42986f-a6d0-40c2-a807-bd4b2ec8472f
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class);

        return products.blockOptional();
    }
}
