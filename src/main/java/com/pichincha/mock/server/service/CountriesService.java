package com.pichincha.mock.server.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@Service
public class CountriesService {

    private final WebClient webClient;

    public CountriesService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://country.io")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .build();
    }

    public Map<String, String> getPhoneCodes() {
        return webClient.get()
                .uri("/phone.json")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToFlux(new ParameterizedTypeReference<Map<String, String>>() {
                }).blockFirst();
    }

    public Map<String, String> getCountryNames() {
        return webClient.get()
                .uri("/names.json")
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToFlux(new ParameterizedTypeReference<Map<String, String>>() {
                }).blockFirst();
    }
}
