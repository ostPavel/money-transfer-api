package com.revolut.transfer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TransactionResponse {

    boolean success;

    @NotNull
    String message;

}
