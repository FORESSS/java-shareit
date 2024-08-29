package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class UserClientConfig {
    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public UserClient getUserClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                .build();
        return new UserClient(restTemplate);
    }
}