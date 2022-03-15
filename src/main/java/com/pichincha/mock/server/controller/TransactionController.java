package com.pichincha.mock.server.controller;

import com.pichincha.mock.server.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "all")
    public ResponseEntity getCountryNames() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), new HttpHeaders(), HttpStatus.OK);
    }
}
