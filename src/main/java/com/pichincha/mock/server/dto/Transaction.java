package com.pichincha.mock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {

    private String transactionId;
    private String description;
    private String longDescription;
    private BigDecimal amount;
    private CodeAndDescription currency;
    private String operationDate;
    private CodeAndDescription type;
    private TransactionChannel channel;
    private Account account;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransactionChannel {

        private CodeAndDescription type;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Account {

        private BigDecimal availableAmount;
        private CodeAndDescription currency;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeAndDescription {

        private String code;
        private String description;
    }
}
