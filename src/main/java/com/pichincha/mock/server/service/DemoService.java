package com.pichincha.mock.server.service;

import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.properties.DemoProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

@Service
@Log4j2
public class DemoService {

    private final WebClient webClient;

    public DemoService(DemoProperties demoProperties) {

        this.webClient = WebClient.builder()
                .baseUrl(demoProperties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();
    }

    public Flux<Transaction> getTransactions() {
        return this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/v3/76cedf38-7f06-43ad-b618-7293dc9b0926").build())
                .retrieve()
                .bodyToFlux(Transaction.class);
    }

    public Optional<Transaction> getTransactionById() {
        Mono<Transaction> products = webClient.get()
                .uri("v3/1d42986f-a6d0-40c2-a807-bd4b2ec8472f")
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(Transaction.class)
                .retryWhen(
                        Retry.backoff(1, Duration.ofMillis(5000))
                                .minBackoff(Duration.ofMillis(5000))
                );

        return products.blockOptional();
    }
}
