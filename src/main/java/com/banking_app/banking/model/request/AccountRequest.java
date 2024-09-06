package com.banking_app.banking.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {

    private String number;
    private String name;
    private BigDecimal balance;
}
