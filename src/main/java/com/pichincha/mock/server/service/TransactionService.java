package com.pichincha.mock.server.service;

import com.pichincha.mock.server.dto.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Service
public class TransactionService {

    private final WebClient webClient;

    private int PORT = 8000;

    public TransactionService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + PORT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:" + PORT))
                .build();
    }

    public Optional<Transaction> getTransaction(String id) {
        Mono<Transaction> products = webClient.get()
                .uri("/api/transactions/" + id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class)
                .retryWhen(
                        Retry.backoff(1, Duration.ofMillis(5000))
                                .minBackoff(Duration.ofMillis(5000))
                );

        return products.blockOptional(Duration.ofSeconds(30));
    }

    public Flux<Transaction> getTransactions() {
        return this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/transactions").build())
                .retrieve()
                .bodyToFlux(Transaction.class);
    }
}
