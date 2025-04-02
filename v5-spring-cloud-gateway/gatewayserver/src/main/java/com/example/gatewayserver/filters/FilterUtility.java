package com.example.gatewayserver.filters;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Optional;

@Component
public class FilterUtility {

    public static final String CORRELATION_ID = "mybank-correlation-id";

    /**
     * Retrieves the correlation ID from the request headers.
     *
     * @param requestHeaders the HTTP headers of the request
     * @return the correlation ID if present, otherwise null
     */
    public String getCorrelationId(HttpHeaders requestHeaders) {
        return Optional.ofNullable(requestHeaders.get(CORRELATION_ID))
                .flatMap(requestHeaderList -> requestHeaderList.stream().findFirst())
                .orElse(null);
    }

    /**
     * Sets a custom header in the request.
     *
     * @param exchange the current server web exchange
     * @param name the name of the header to set
     * @param value the value of the header to set
     * @return the mutated server web exchange with the new header
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }
}