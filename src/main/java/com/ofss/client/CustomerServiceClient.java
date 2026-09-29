package com.ofss.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.ofss.exception.BadRequestException;

@Component
public class CustomerServiceClient {

    private final RestClient restClient;

    public CustomerServiceClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        this.restClient = restClientBuilder.build();
    }

    public void validateCustomer(
            Long customerId,
            String authorizationHeader
    ) {
        try {
            restClient.get()
                    .uri(
                            "http://customer/api/customers/{customerId}",
                            customerId
                    )
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .retrieve()
                    .toBodilessEntity();

        } catch (HttpClientErrorException.NotFound exception) {
            throw new BadRequestException(
                    "Customer with ID " + customerId + " does not exist"
            );
        }
    }
}
