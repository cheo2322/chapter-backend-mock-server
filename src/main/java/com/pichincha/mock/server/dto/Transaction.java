package com.pichincha.mock.server.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  private String transactionId;
  private String description;
  private String longDescription;
  private BigDecimal amount;
  private CodeAndDescription currency;
  private String operationDate;
  private CodeAndDescription type;
  private TransactionChannel channel;
  private Account account;


  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TransactionChannel {

    private CodeAndDescription type;
    private String name;
  }

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Account {

    private BigDecimal availableAmount;
    private CodeAndDescription currency;
  }

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CodeAndDescription {

    private String code;
    private String description;
  }
}
