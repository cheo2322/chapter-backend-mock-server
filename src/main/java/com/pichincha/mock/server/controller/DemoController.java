package com.pichincha.mock.server.controller;

import com.pichincha.mock.server.dto.Transaction;
import com.pichincha.mock.server.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/default")
public class DemoController {

    @Autowired
    DemoService service;

    @GetMapping("/transactions")
    public ResponseEntity<Flux<Transaction>> getTransactions() {
        return ResponseEntity.ok().body(this.service.getTransactions());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Transaction result = this.service.getTransactionById().get();
        result.setTransactionId(id);
        return ResponseEntity.ok().body(result);
    }
}
