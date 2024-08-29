package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RequestClientConfig {
    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public RequestClient getRequestClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                .build();
        return new RequestClient(restTemplate);
    }
}