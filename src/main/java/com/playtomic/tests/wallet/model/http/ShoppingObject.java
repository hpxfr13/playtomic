package com.playtomic.tests.wallet.model.http;

import com.playtomic.tests.wallet.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class ShoppingObject {
    @Id
    String id;
    String creditCardNumber;
    Product product;
    Date eventDate;
    OrderStatus orderStatus;
}
