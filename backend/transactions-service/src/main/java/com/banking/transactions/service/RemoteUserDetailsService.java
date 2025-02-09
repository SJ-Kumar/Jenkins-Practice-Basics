package com.banking.transactions.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RemoteUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteUserDetailsService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String AUTH_SERVICE_URL = "http://localhost:8081/auth/user/";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.info("🔄 Fetching user details from Authentication Service for: " + username);
            
            Map<String, String> response = restTemplate.getForObject(AUTH_SERVICE_URL + username, Map.class);
            logger.info("✅ Received response: " + response);

            if (response == null || !response.containsKey("username")) {
                logger.error("❌ User not found in Authentication Service.");
                throw new UsernameNotFoundException("User not found");
            }

            logger.info("✅ Successfully retrieved user: " + response.get("username"));

            return User.builder()
                    .username(response.get("username"))
                    .password(response.get("password")) // Already encoded
                    .roles(response.get("role")) // Example: "USER"
                    .build();

        } catch (HttpClientErrorException e) {
            logger.error("❌ Authentication Service responded with an error: " + e.getStatusCode());
            throw new UsernameNotFoundException("User authentication failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected error: " + e.getMessage());
            throw new UsernameNotFoundException("User authentication failed: " + e.getMessage());
        }
    }
}
