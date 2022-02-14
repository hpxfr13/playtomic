package com.playtomic.tests.wallet.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Document
public class WalletRequest {

    @Id
    String id;

    @NonNull
    String creditCardNumber;

    @NonNull
    BigDecimal amount;
}
