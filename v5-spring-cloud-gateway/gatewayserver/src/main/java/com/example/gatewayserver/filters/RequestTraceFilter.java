package com.example.gatewayserver.filters;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
    private final FilterUtility filterUtility;

    @Autowired
    public RequestTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    /**
     * Filters the incoming request to check for a correlation ID.
     * If not present, generates a new correlation ID and sets it in the request headers.
     *
     * @param exchange the current server web exchange
     * @param chain the gateway filter chain
     * @return a Mono that indicates when the filter processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        if (!isCorrelationIdPresent(httpHeaders)) {
            String correlationId = generateCorrelationId();
            filterUtility.setCorrelationId(exchange, correlationId);
            logger.info("mybank-correlation-id generated: {}", correlationId);
        } else {
            String correlationId = filterUtility.getCorrelationId(httpHeaders);
            logger.info("mybank-correlation-id found in request: {}", correlationId);
        }
        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders httpHeaders) {
        return filterUtility.getCorrelationId(httpHeaders) != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
