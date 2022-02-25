package com.pichincha.mock.server.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemoRequestDto {
    private List<Transacction> transacctions;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Transacction {
        private String transactionId;
        private String description;
        private String longDescription;
        private BigDecimal amount;
        private CodeAndDescription currency;
        private String operationDate;
        private CodeAndDescription type;
        private TranssactionChannel channel;
        private Account account;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class TranssactionChannel {
        private CodeAndDescription type;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Account {
        private BigDecimal availableAmount;
        private CodeAndDescription currency;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class CodeAndDescription {
        private String code;
        private String description;
    }
}
