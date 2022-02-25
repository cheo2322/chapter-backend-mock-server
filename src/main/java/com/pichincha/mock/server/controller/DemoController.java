package com.pichincha.mock.server.controller;

import com.pichincha.mock.server.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/default")
public class DemoController {

  @Autowired
  DemoService service;

  @GetMapping("/transactions")
  public void getTransactions() {
    this.service.getTransactions();
  }

}
