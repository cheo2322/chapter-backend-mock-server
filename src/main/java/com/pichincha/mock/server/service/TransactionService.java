package com.pichincha.mock.server.service;

import com.pichincha.mock.server.dto.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

@Service
public class TransactionService {

    private final WebClient webClient;
    private final int PORT = 9000;

    public TransactionService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + PORT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();
    }

    public Transaction addTransaction(Transaction data) {
        Mono<Transaction> response = webClient.post()
                .uri("/api/transactions")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(data), Transaction.class)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class);

        return response.blockOptional().get();
    }

    public Optional<Transaction> getTransaction(String id) {
        Mono<Transaction> response = webClient.get()
                .uri("/api/transactions/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class);

        return response.blockOptional();
    }

    public Flux<Transaction> getAllTransactions() {
        return this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/transactions").build())
                .retrieve()
                .bodyToFlux(Transaction.class);
    }

    public String deleteTransaction(String id) {
        Mono<String> response = webClient.delete()
                .uri("/api/transactions/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(String.class);

        return response.blockOptional().get();
    }
}
