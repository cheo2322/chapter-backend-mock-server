package com.pichincha.mock.server.service;

import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.webclient.DemoWebClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Log4j2
public class DemoService {

  @Autowired
  DemoWebClient webClient;

  public Flux<Transaction> getTransactions() {
    return webClient.getTransactions();
  }
}
