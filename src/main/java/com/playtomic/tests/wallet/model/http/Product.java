package com.playtomic.tests.wallet.model.http;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
public class Product {

    @Id
    String id;

    @Indexed(unique = true)
    String name;

    BigDecimal price;

}
