package com.banking.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AccountServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceClient.class);

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8082/accounts";

    public boolean updateBalance(String username, double newBalance) {
        try {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
            URI url = new URI(ACCOUNT_SERVICE_URL + "/updateBalance?username=" + encodedUsername + "&newBalance=" + newBalance);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Boolean.class);

            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            logger.error("Error updating balance for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public double getBalance(String username) {
        try {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
            String url = ACCOUNT_SERVICE_URL + "/balance?username=" + encodedUsername;

            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);

            return response.getBody() != null ? response.getBody() : 0.0;
        } catch (Exception e) {
            logger.error("Error fetching balance for user {}: {}", username, e.getMessage());
            return 0.0;
        }
    }
}
