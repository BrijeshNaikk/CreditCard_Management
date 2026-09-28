package com.ofss.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.ofss.exceptions.BadRequestException;

import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class SecurityServiceClient {

    private final RestClient restClient;

    public SecurityServiceClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        this.restClient = restClientBuilder.build();
    }

    public SecurityUserResponse getUserById(
            Long userId,
            String authorizationHeader
    ) {

        try {
            SecurityUserResponse user = restClient.get()
            		.uri("http://security-service/api/auth/users/{userId}", userId)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            authorizationHeader
                    )
                    .retrieve()
                    .body(SecurityUserResponse.class);

            if (user == null) {
                throw new BadRequestException(
                        "User with ID " + userId
                                + " is not present in APP_USERS table"
                );
            }

            return user;

        } catch (HttpClientErrorException.NotFound exception) {
            throw new BadRequestException(
                    "User with ID " + userId
                            + " is not present in APP_USERS table"
            );
        }
    }
}
