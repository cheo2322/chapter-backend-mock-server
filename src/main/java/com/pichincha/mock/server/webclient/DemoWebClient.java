package com.pichincha.mock.server.webclient;

import com.pichincha.mock.server.dto.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

@Component
public class DemoWebClient {

    private final WebClient webClient;
    private final int PORT = 9000;

    public DemoWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + PORT)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();
    }

    public Optional<Transaction> getTransaction(String id) {
        Mono<Transaction> products = webClient.get()
                .uri("api/transactions/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class);

        return products.blockOptional();
    }
}
