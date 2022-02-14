package com.playtomic.tests.wallet.model.http;

import lombok.Builder;

@Builder
public class ResponseErrorObject {
    String message;
    int statusCode;
}
