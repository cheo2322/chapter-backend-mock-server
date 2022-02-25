package com.pichincha.mock.server.service;

import com.pichincha.mock.server.webclient.DemoWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

  @Autowired
  DemoWebClient webClient;

  public void getTransactions() {

  }

}
